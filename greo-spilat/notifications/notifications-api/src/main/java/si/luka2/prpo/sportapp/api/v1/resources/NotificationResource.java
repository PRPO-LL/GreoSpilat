package si.luka2.prpo.sportapp.api.v1.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import si.luka2.prpo.sportapp.beans.NotificationBean;
import si.luka2.prpo.sportapp.entities.Notification;

import java.util.List;

@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationBean notificationBean;

    @Context
    private UriInfo uriInfo;

    @Operation(summary = "Create a single notification", description = "Adds a new notification for a user or group")
    @APIResponse(responseCode = "201", description = "Notification created successfully")
    @POST
    public Response createNotification(Notification notification) {
        Notification created = notificationBean.addNotification(notification);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Operation(summary = "Create bulk notifications", description = "Adds multiple notifications at once")
    @APIResponse(responseCode = "201", description = "Bulk notifications created successfully")
    @POST
    @Path("/bulk")
    public Response createBulkNotifications(List<Notification> notifications) {
        int createdCount = notificationBean.addBulkNotifications(notifications);
        return Response.status(Response.Status.CREATED)
                .entity("Successfully created " + createdCount + " notifications.")
                .build();
    }

    @Operation(summary = "Get all notifications for a user", description = "Fetches all notifications for a specific user")
    @APIResponse(responseCode = "200", description = "List of user notifications")
    @GET
    @Path("/user/{userId}")
    public Response getUserNotifications(@PathParam("userId") int userId) {
        List<Notification> notifications = notificationBean.getUserNotifications(userId);
        return Response.ok(notifications).build();
    }

    @Operation(summary = "Get a specific notification", description = "Fetches a notification by its ID")
    @APIResponse(responseCode = "200", description = "Notification details")
    @GET
    @Path("/{id}")
    public Response getNotification(@PathParam("id") int id) {
        Notification notification = notificationBean.getNotification(id);
        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Notification not found").build();
        }
        return Response.ok(notification).build();
    }

    @Operation(summary = "Retry failed notifications", description = "Retries sending all failed notifications")
    @APIResponse(responseCode = "200", description = "Retry operation completed")
    @POST
    @Path("/retry")
    @RolesAllowed("admin")
    public Response retryFailedNotifications() {
        int retriedCount = notificationBean.retryFailedNotifications();
        return Response.ok("Successfully retried " + retriedCount + " notifications.").build();
    }

    @Operation(summary = "Delete a notification", description = "Deletes a specific notification by its ID")
    @APIResponse(responseCode = "204", description = "Notification deleted successfully")
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteNotification(@PathParam("id") int id) {
        boolean deleted = notificationBean.deleteNotification(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Failed to delete notification. ID not found.")
                    .build();
        }
        return Response.noContent().build();
    }
}
