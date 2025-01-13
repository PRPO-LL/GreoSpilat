package si.luka2.prpo.sportapp.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.luka2.prpo.sportapp.beans.JoinBean;
import si.luka2.prpo.sportapp.entities.Joins;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

@Path("/join")
@Consumes("application/json")
@Produces("application/json")
public class JoinResource {
    private Logger log = Logger.getLogger(JoinResource.class.getName());
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    private JoinBean joinBean;

    @Operation(summary = "Join an event", description = "Associates a user with an event, indicating they have joined it.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Successfully joined the event."),
            @APIResponse(responseCode = "400", description = "Invalid user or event ID."),
            @APIResponse(responseCode = "409", description = "Event is already full."),
            @APIResponse(responseCode = "500", description = "Failed to process the join request due to a server error.")
    })
    @POST
    public Response joinEvent(@QueryParam("userId") int userId, @QueryParam("eventId") int eventId) {
        if (userId <= 0 || eventId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid user ID or event ID. Both must be positive integers.")
                    .build();
        }

        try {
            Joins join = joinBean.addJoin(userId, eventId);

            String basePath = "http://event-service:8083/v1/events/";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(basePath + eventId + "/participants?action=join"))
                    .method("PUT", HttpRequest.BodyPublishers.noBody())
                    .header("Content-Type", "application/json")
                    .build();
            log.info("Calling Event API at URL: " + basePath + eventId + "/participants?action=join");

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            log.warning("Event API responded with status: " + status);

            if (status >= 200 && status < 300) {
                return Response.status(Response.Status.CREATED).entity(join).build();
            } else if (status == 409) {
                joinBean.removeJoin(userId, eventId);
                return Response.status(Response.Status.CONFLICT)
                        .entity("Event is already full. Could not join.")
                        .build();
            } else {
                joinBean.removeJoin(userId, eventId);
                String msg = "Failed to update participant count for event " + eventId + ". Event API responded with status: " + status;
                log.warning(msg);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
            }
        } catch (IOException | InterruptedException e) {
            log.severe("HTTP call failed: " + e.getMessage());
            joinBean.removeJoin(userId, eventId);
            String msg = "Error occurred while calling Event API: " + e.getMessage();
            log.severe(msg);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to process the join request due to server error.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @Operation(summary = "Get all joins for an event", description = "Fetches all users who have joined a specific event.")
    @APIResponse(responseCode = "200", description = "List of user IDs who joined the event retrieved successfully.")
    @APIResponse(responseCode = "204", description = "No users have joined the event.")
    @APIResponse(responseCode = "400", description = "Invalid event ID provided.")
    @GET
    @Path("/event/{eventId}")
    public Response getJoinsByEvent(@PathParam("eventId") int eventId) {
        if (eventId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid event ID. Event ID must be a positive integer.")
                    .build();
        }

        List<Joins> joins = joinBean.getJoinsByEvent(eventId);
        if (joins == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("No users have joined the event.")
                    .build();
        }

        // Extract user IDs into a JSON array
        JsonArrayBuilder userIdsArrayBuilder = Json.createArrayBuilder();
        for (Joins join : joins) {
            if (join.getUser() != null) {
                userIdsArrayBuilder.add(join.getUser().getUser_id());
            }
        }

        JsonArray userIdsArray = userIdsArrayBuilder.build();
        return Response.ok()
                .header("User-IDs", userIdsArray.toString())
                .build();
    }

    @Operation(summary = "Get all joins for a user", description = "Fetches all events joined by a specific user.")
    @APIResponse(responseCode = "200", description = "List of events joined by the user retrieved successfully.")
    @APIResponse(responseCode = "204", description = "User has not joined any events.")
    @APIResponse(responseCode = "400", description = "Invalid user ID provided.")
    @GET
    @Path("/user/{userId}")
    public Response getJoinsByUser(@PathParam("userId") int userId) {
        if (userId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid user ID. User ID must be a positive integer.")
                    .build();
        }

        List<Joins> joins = joinBean.getJoinsByUser(userId);
        if (joins == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("User has not joined any events.")
                    .build();
        }

        return Response.ok(joins).build();
    }

    @DELETE
    @Path("/{userId}/event/{eventId}")
    @Operation(summary = "Leave an event", description = "Removes the join relationship between a user and an event, indicating the user has left the event, and decrements the event's participant count.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Successfully left the event."),
            @APIResponse(responseCode = "400", description = "Invalid user or event ID."),
            @APIResponse(responseCode = "404", description = "Join relationship or event not found."),
            @APIResponse(responseCode = "500", description = "Failed to leave the event due to server error.")
    })
    public Response leaveEvent(@PathParam("userId") int userId, @PathParam("eventId") int eventId) {
        if (userId <= 0 || eventId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid user ID or event ID. Both must be positive integers.")
                    .build();
        }

        try {
            // Remove the join relationship
            boolean removed = joinBean.removeJoin(userId, eventId);
            if (!removed) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Join relationship between user " + userId + " and event " + eventId + " not found.")
                        .build();
            }

            String basePath = "http://event-service:8083/v1/events/";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(basePath + eventId + "/participants?action=leave"))
                    .method("PUT", HttpRequest.BodyPublishers.noBody())
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                // Success
                return Response.noContent().build();
            } else {
                // Handle Event API failure
                String msg = "Failed to update participant count for event " + eventId + ". Event API responded with status: " + status;
                log.warning(msg);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(msg)
                        .build();
            }
        } catch (IOException | InterruptedException e) {
            String msg = "Error occurred while calling Event API: " + e.getMessage();
            log.severe(msg);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to leave the event due to server error.")
                    .build();
        }
    }

}
