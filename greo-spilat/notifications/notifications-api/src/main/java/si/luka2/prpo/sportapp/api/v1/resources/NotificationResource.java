package si.luka2.prpo.sportapp.api.v1.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notifications")
public class NotificationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotifications() {
        return Response.ok("{\"notifications\": [\"Notification 1\", \"Notification 2\"]}").build();
    }
}
