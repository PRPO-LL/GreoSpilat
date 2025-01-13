package si.luka2.prpo.sportapp.api.v1.resources;


import com.kumuluz.ee.rest.beans.QueryParameters;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.luka2.prpo.sportapp.entities.Event;
import si.luka2.prpo.sportapp.beans.EventsBean;
import si.luka2.prpo.sportapp.beans.JwtService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
    private Logger log = Logger.getLogger(Event.class.getName());
    private CloseableHttpClient httpClient;
    private String basePath;
    private final static int adminId = 1;
    @Context
    private UriInfo uriInfo;

    @Inject
    private EventsBean eventsBean;

    @Operation(description = "Vrne seznam vseh eventov")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam eventov",
                    content = @Content(schema = @Schema(implementation = Event.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih eventov")}
            )})
    @RolesAllowed("user")
    @GET
    public Response getEvents(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        System.out.println("Sparsani QueryParameters: " + query.toString());
        List<Event> events = eventsBean.getEvents(query);
        Long totalCount = eventsBean.getEventsCount(null);

        return Response.ok(events, MediaType.APPLICATION_JSON)
                .header("X-Total-Count", totalCount)
                .build();
    }

    @Operation(description = "Vrne en event", summary = "event")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En event",
                    content = @Content(schema = @Schema(implementation = Event.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
            description = "Event ni bil najden")
    })
    @RolesAllowed("user")
    @GET
    @Path("{id}")
    public Response getEvent(@PathParam("id") int eventId) {
        Event event = eventsBean.getEvent(eventId);
        if(event == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Uporabnik ni bil najden")
                    .build();
        }
        return Response.ok(event, MediaType.APPLICATION_JSON).build();
    }

    @Operation(description = "Doda event.", summary = "Dodajanje")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Event uspešno dodan",
                    content = @Content(schema = @Schema(implementation = Event.class, type = SchemaType.OBJECT))),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri dodajanju eventa")
    })
    @RolesAllowed("user")
    @POST
    @Path("/add")
    public Response addEvent(@Context HttpHeaders headers, Event event) { //argument ki je passan tukaj, se doda cez klic APIja
        httpClient = HttpClientBuilder.create().build();
        String authPath = "http://auth-service:8085/v1/";
        String joinPath = "http://join-service:8403/v1/";
        String header = headers.getHeaderString("Authorization");

        Event novi = null;
        int id = -1;
        if (header != null) {
            //int id = JwtService.validateToken(header);
            try {
                HttpPost post = new HttpPost(authPath + "auth/validate");
                post.setHeader("Authorization", header);
                HttpResponse httpResponse = httpClient.execute(post);

                int code = httpResponse.getCode();
                if (code == 200) {
                    org.apache.hc.core5.http.Header idHeader = httpResponse.getHeader("Id");
                    id = Integer.parseInt(idHeader.getValue());
                }
            } catch (IOException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new InternalServerErrorException(e);
            } catch (ProtocolException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new RuntimeException(e);
            }
        }
        if(id > 0){
            event.setCreatorId(id);
            novi = eventsBean.addEvent(event); // recimo v postmanu ko mas body POSTa
            Integer eventId = novi.getIid();

            try {
                HttpPost post = new HttpPost(joinPath + "join");
                post.setHeader("Authorization", header);

                // Add body with userId and eventId
                String jsonBody = "{\"userId\": " + id + ", \"eventId\": " + eventId + "}";
                post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

                HttpResponse httpResponse = httpClient.executeOpen(null, post, null);

                int code = httpResponse.getCode();
                if (code == 200) {
                    org.apache.hc.core5.http.Header idHeader = httpResponse.getHeader("Id");
                    id = Integer.parseInt(idHeader.getValue());
                }
            } catch (IOException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new InternalServerErrorException(e);
            } catch (ProtocolException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new RuntimeException(e);
            }
        }
        if(novi == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Napaka pri dodajanju eventa")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(novi)
                .build();
    }

    @Operation(description = "posodabljanje Uporabnika", summary = "posodabljanje")
    @APIResponses({
            @APIResponse(description = "Uporabnik uspešno posodobljen",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Event.class, type = SchemaType.OBJECT))),

            @APIResponse(responseCode = "304",
                    description = "Napaka pri posodabljanju uporabnika")

    })
    @RolesAllowed("user")
    @PATCH
    @Path("/update/{id}")
    public Response updateEvent(@Context HttpHeaders headers,@PathParam("id") int eventId,Event event) {
        httpClient = HttpClientBuilder.create().build();
        basePath = "http://auth-service:8085/v1/";
        String header = headers.getHeaderString("Authorization");
        Event novi = null;
        Event stari = eventsBean.getEvent(eventId);
        int id = -1;
        if (header != null) {
//            int id = JwtService.validateToken(header);
            try {
                HttpPost post = new HttpPost(basePath + "auth/validate");
                post.setHeader("Authorization", header);
                HttpResponse httpResponse = httpClient.executeOpen(null, post, null);

                int code = httpResponse.getCode();
                if (code == 200) {
                    org.apache.hc.core5.http.Header idHeader = httpResponse.getHeader("Id");
                    id = Integer.parseInt(idHeader.getValue());
                }
            } catch (IOException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new InternalServerErrorException(e);
            } catch (ProtocolException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new RuntimeException(e);
            }
            if(id == stari.getCreatorId() || id == adminId)
                novi = eventsBean.updateEvent(eventId,event); // recimo v postmanu ko mas body POSTa
        }
        if(novi == null){
            return Response.status(Response.Status.NOT_MODIFIED)
                    .entity("Spodletel poskus posodobitve eventa")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(novi)
                .build();
    }

    @Operation(description = "brisanje eventa", summary = "brisanje")
    @APIResponses({
            @APIResponse(description = "Event uspešno izbrisan",
                    responseCode = "204"),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri brisanju eventa")

    })
    @RolesAllowed("user")
    @DELETE
    @Path("/delete/{id}")
    public Response deleteEvent(@Context HttpHeaders headers,@PathParam("id") int eventId) {
        httpClient = HttpClientBuilder.create().build();
        basePath = "http://auth-service:8085/v1/";
        String header = headers.getHeaderString("Authorization");
        Event novi = eventsBean.getEvent(eventId);
        boolean zbrisan = false;
        int id = -1;
        if (header != null) {
//            int id = JwtService.validateToken(header);
            try {
                HttpPost post = new HttpPost(basePath + "auth/validate");
                post.setHeader("Authorization", header);
                HttpResponse httpResponse = httpClient.executeOpen(null, post, null);

                int code = httpResponse.getCode();
                if (code == 200) {
                    org.apache.hc.core5.http.Header idHeader = httpResponse.getHeader("Id");
                    id = Integer.parseInt(idHeader.getValue());
                }
            } catch (IOException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new InternalServerErrorException(e);
            } catch (ProtocolException e) {
                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                log.info(msg);
                throw new RuntimeException(e);
            }
            if(id == novi.getCreatorId() || id == adminId)
                zbrisan = eventsBean.deleteEvent(eventId);
        }
        if(!zbrisan){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Napaka pri brisanju eventa")
                    .build();
        }

        return Response.noContent().build();
    }

    @Operation(summary = "Update event participants", description = "Increments or decrements the participant counter of an event when a user joins or leaves.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Participant count updated successfully."),
            @APIResponse(responseCode = "400", description = "Invalid event ID or action."),
            @APIResponse(responseCode = "404", description = "Event not found."),
            @APIResponse(responseCode = "409", description = "Event is already full!"),
            @APIResponse(responseCode = "500", description = "Failed to update participant count due to server error.")
    })
    @RolesAllowed("user")
    @PUT
    @Path("/{id}/participants")
    public Response updateParticipants(@PathParam("id") int eventId, @QueryParam("action") String action) {
        String notificationPath = "http://notifications-service:8401/v1/";

        if (eventId <= 0 || action == null || (!action.equalsIgnoreCase("join") && !action.equalsIgnoreCase("leave"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid event ID or action. Action must be 'join' or 'leave'.")
                    .build();
        }

        Event event = eventsBean.getEvent(eventId);
        if (event == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Event with ID " + eventId + " not found.")
                    .build();
        }

        try {
            boolean updated = false;

            if (action.equalsIgnoreCase("join")) {
                updated = eventsBean.incrementParticipants(eventId);
            } else if (action.equalsIgnoreCase("leave")) {
                updated = eventsBean.decrementParticipants(eventId);
            }

            if (!updated) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Failed to update participant count. Check the event state.")
                        .build();
            }

            Event novi = eventsBean.getEvent(eventId);
            if (novi == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Event with ID after inc/dec" + eventId + " not found.")
                        .build();
            }

            Integer participants = novi.getParticipants();
            Integer maxParticipants = novi.getMaxParticipants();
            List<Integer> userIds = null;
            if (participants == maxParticipants) {
                try {
                    String joinServicePath = "http://localhost:8403/v1/join/event/" + novi.getIid();
                    HttpGet getRequest = new HttpGet(joinServicePath);
                    HttpResponse joinResponse = httpClient.execute(getRequest);

                    if (joinResponse.getCode() == 200) {
                        String userIdsHeader = joinResponse.getHeader("User-IDs").getValue();
                        userIds = new ArrayList<>();

                        if (userIdsHeader != null && !userIdsHeader.isEmpty()) {
                            for (String userId : userIdsHeader.split(",")) {
                                userIds.add(Integer.parseInt(userId.trim()));
                            }
                        }
                        log.info("User IDs retrieved from header: " + userIds);
                    } else {
                        log.warning("Failed to fetch user IDs from join service. Status: " + joinResponse.getCode());
                    }

                    if (!userIds.isEmpty()) {
                        List<JsonObject> notifications = new ArrayList<>();
                        for (Integer userId : userIds) {
                            JsonObject notification = Json.createObjectBuilder()
                                    .add("recipientId", userId)
                                    .add("recipientType", "USER")
                                    .add("message", "Event " + novi.getTitle() + " you joined is filled! HF")
                                    .add("type", "ALERT")
                                    .build();
                            notifications.add(notification);
                        }

                        JsonArray notificationArray = Json.createArrayBuilder(notifications).build();

                        HttpPost bulkNotificationPost = new HttpPost(notificationPath + "notifications/bulk");
                        bulkNotificationPost.setEntity(new StringEntity(notificationArray.toString(), ContentType.APPLICATION_JSON));

                        HttpResponse notificationResponse = httpClient.executeOpen(null, bulkNotificationPost, null);
                        if (notificationResponse.getCode() == 201) {
                            log.info("Bulk notifications sent successfully.");
                        } else {
                            log.warning("Failed to send bulk notifications. Status: " + notificationResponse.getCode());
                        }
                    }
                } catch (IOException e) {
                    String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                    log.info(msg);
                    throw new InternalServerErrorException(e);
                } catch (ProtocolException e) {
                    String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
                    log.info(msg);
                    throw new RuntimeException(e);
                }
            }

            return Response.ok(novi).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update participant count due to server error.")
                    .build();
        }
    }



}
