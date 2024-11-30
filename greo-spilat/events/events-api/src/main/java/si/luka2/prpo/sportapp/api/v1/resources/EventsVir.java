package si.luka2.prpo.sportapp.api.v1.viri;


import com.kumuluz.ee.rest.beans.QueryParameters;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.luka2.prpo.sportapp.entities.Event;
import si.luka2.prpo.sportapp.beans.EventsZrno;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventsVir {

    @Context
    private UriInfo uriInfo;

    @Inject
    private EventsZrno eventsZrno;

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

        List<Event> events = eventsZrno.getEvents();
        Long totalCount = eventsZrno.getEventsCount(null);

        return Response.ok(events, MediaType.APPLICATION_JSON)
                .header("X-Total-Count", totalCount)
                .build();
    }

    @Operation(description = "Vrne en event", summary = "event")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En event",
                    content = @Content(schema = @Schema(implementation = Event.class, type = SchemaType.OBJECT))
            )})
    @RolesAllowed("user")
    @GET
    @Path("{id}")
    public Response getEvent(@PathParam("id") int eventId) {
        Event event = eventsZrno.getEvent(eventId);
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
    @Path("/dodaj")
    public Response addEvent(Event event) { //argument ki je passan tukaj, se doda cez klic APIja
        Event novi = eventsZrno.addEvent(event); // recimo v postmanu ko mas body POSTa

        if(novi == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Spodletel poskus kreacije eventa")
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

            @APIResponse(responseCode = "400",
                    description = "Napaka pri posodabljanju uporabnika")

    })
    @RolesAllowed("user")
    @PATCH
    @Path("/posodobi/{id}")
    public Response updateEvent(@PathParam("id") int eventId,Event event) {

        Event novi = eventsZrno.updateEvent(eventId, event);
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
    @Path("/odstrani/{id}")
    public Response deleteEvent(@PathParam("id") int eventId) {

        boolean novi = eventsZrno.deleteEvent(eventId);
        if(!novi){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Spodletel poskus brisanja eventa")
                    .build();
        }

        return Response.noContent().build();
    }

}
