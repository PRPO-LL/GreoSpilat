package si.luka2.prpo.sportapp.api.v1.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import si.luka2.prpo.sportapp.beans.NotificationBean;
import si.luka2.prpo.sportapp.entities.Notification;

import java.util.List;

@ApplicationScoped
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
    @APIResponse(responseCode = "400", description = "Invalid notification data")
    @APIResponse(responseCode = "500", description = "Failed to create notification")
    @POST
    public Response createNotification(Notification notification) {
        if (notification == null || notification.getRecipientId() == null || notification.getMessage() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid notification data. RecipientId and message are required.")
                    .build();
        }

        Notification created = notificationBean.addNotification(notification);
        if (created == null || created.getId() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create notification due to server error.")
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Operation(summary = "Create bulk notifications", description = "Adds multiple notifications at once")
    @APIResponse(responseCode = "201", description = "All notifications created successfully")
    @APIResponse(responseCode = "400", description = "Invalid notification data")
    @APIResponse(responseCode = "207", description = "Partial success: Some notifications failed")
    @APIResponse(responseCode = "500", description = "Failed to create notifications")
    @POST
    @Path("/bulk")
    public Response createBulkNotifications(@Valid List<Notification> notifications) {
        // Validation: Ensure list is not null or empty
        if (notifications == null || notifications.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Notification list cannot be null or empty.")
                    .build();
        }

        // Call the business logic to add notifications
        int createdCount = notificationBean.addBulkNotifications(notifications);
        int totalCount = notifications.size();

        if (createdCount == 0) {
            // No notifications were created
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create all notifications.")
                    .build();
        }

        if (createdCount < totalCount) {
            // Partial success
            return Response.status(207) // HTTP 207 Multi-Status
                    .entity("Successfully created " + createdCount + " out of " + totalCount + " notifications.")
                    .build();
        }

        // All notifications were successfully created
        return Response.status(Response.Status.CREATED)
                .entity("Successfully created all " + createdCount + " notifications.")
                .build();
    }


    @Operation(summary = "Get all notifications for a user", description = "Fetches all notifications for a specific user")
    @APIResponse(responseCode = "200", description = "List of user notifications retrieved successfully")
    @APIResponse(responseCode = "204", description = "No notifications found for the given user ID")
    @APIResponse(responseCode = "400", description = "Invalid user ID provided")
    @GET
    @Path("/user/{userId}")
    public Response getUserNotifications(@PathParam("userId") int userId) {
        // Validate the userId
        if (userId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid user ID. User ID must be a positive integer.")
                    .build();
        }

        List<Notification> notifications = notificationBean.getUserNotifications(userId);

        if (notifications == null || notifications.isEmpty()) {
            // No content if the user has no notifications
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("No notifications found for the given user ID.")
                    .build();
        }

        // Return the list of notifications with HTTP 200 OK
        return Response.ok(notifications).build();
    }


    @Operation(summary = "Get a specific notification", description = "Fetches a notification by its ID")
    @APIResponse(responseCode = "200", description = "Notification details retrieved successfully")
    @APIResponse(responseCode = "400", description = "Invalid notification ID provided")
    @APIResponse(responseCode = "404", description = "Notification not found")
    @GET
    @Path("/{id}")
    public Response getNotification(@PathParam("id") int id) {
        // Validate the ID
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid notification ID. ID must be a positive integer.")
                    .build();
        }

        // Fetch the notification
        Notification notification = notificationBean.getNotification(id);

        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Notification with ID " + id + " not found.")
                    .build();
        }

        return Response.ok(notification).build();
    }


    @Operation(summary = "Retry failed notifications", description = "Retries sending all failed notifications")
    @APIResponse(responseCode = "200", description = "Successfully retried failed notifications")
    @APIResponse(responseCode = "204", description = "No failed notifications to retry")
    @APIResponse(responseCode = "500", description = "An error occurred while retrying notifications")
    @POST
    @Path("/retry")
    @RolesAllowed("admin")
    public Response retryFailedNotifications() {
        try {
            int retriedCount = notificationBean.retryFailedNotifications();

            if (retriedCount == 0) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("No failed notifications to retry.")
                        .build();
            }

            return Response.ok("Successfully retried " + retriedCount + " notifications.").build();
        } catch (Exception e) {
            // Log the error
            // LOGGER.error("Error while retrying failed notifications: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while retrying notifications.")
                    .build();
        }
    }


    @Operation(summary = "Delete a notification", description = "Deletes a specific notification by its ID")
    @APIResponse(responseCode = "204", description = "Notification deleted successfully")
    @APIResponse(responseCode = "400", description = "Invalid notification ID provided")
    @APIResponse(responseCode = "404", description = "Notification not found")
    @APIResponse(responseCode = "500", description = "Failed to delete notification due to server error")
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteNotification(@PathParam("id") int id) {
        // Validate the ID
        if (id <= 0) {
            //LOGGER.warn("Attempted to delete a notification with an invalid ID: {}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid notification ID. ID must be a positive integer.")
                    .build();
        }

        try {
            boolean deleted = notificationBean.deleteNotification(id);

            if (!deleted) {
                //LOGGER.warn("Failed to delete notification with ID: {}", id);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Failed to delete notification. ID not found.")
                        .build();
            }

            //LOGGER.info("Notification with ID {} deleted successfully.", id);
            return Response.noContent().build();

        } catch (Exception e) {
            //LOGGER.error("Error occurred while deleting notification with ID {}: {}", id, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete notification due to server error.")
                    .build();
        }
    }

}
