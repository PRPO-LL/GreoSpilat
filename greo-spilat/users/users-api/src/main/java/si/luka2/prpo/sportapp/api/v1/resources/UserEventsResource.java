package si.luka2.prpo.sportapp.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.luka2.prpo.sportapp.beans.UserEventsBean;
import si.luka2.prpo.sportapp.entities.User;
import si.luka2.prpo.sportapp.entities.UserEvents;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserEventsResource {

    private Logger log = Logger.getLogger(UserEventsResource.class.getName());

    @Context
    private UriInfo uriInfo;

    @Inject
    private UserEventsBean userEventsBean;

    @Operation(description = "Adds user event")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "adds user",
                    content = @Content(schema = @Schema(implementation = UserEvents.class, type = SchemaType.OBJECT))
            )})
    @RolesAllowed("user")
    @Path("/addUserEvent")
    @PUT
    public Response addUserEvent(UserEvents userEvent) {
        if(userEvent == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("userEvent neveljaven")
                    .build();
        }

        UserEvents newUserEvent = userEventsBean.addUserEvent(userEvent);

        if(newUserEvent == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("splodletel poskus dodajanja userEventa")
                    .build();
        }

        return Response.ok(newUserEvent)
                .entity(newUserEvent)
                .build();
    }

    @Operation(description = "Returns user events for one user")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Return users events",
                    content = @Content(schema = @Schema(implementation = UserEvents.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih eventov za uporabnika")}
            )})
    @RolesAllowed("user")
    @GET
    @Path("/{id}/getEvents")
    public Response getUserEvents(@PathParam("id") Integer id) {

        List<UserEvents> userEvents = userEventsBean.getUserEvents(id);
        Long totalCount = userEventsBean.getUserEventsCount(id);

        return Response.ok(userEvents, MediaType.APPLICATION_JSON)
                .header("X-Total-Count", totalCount)
                .build();
    }

}
