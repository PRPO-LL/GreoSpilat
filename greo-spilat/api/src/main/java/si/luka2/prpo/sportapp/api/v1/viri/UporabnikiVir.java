package si.luka2.prpo.sportapp.api.v1.viri;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import com.kumuluz.ee.rest.beans.QueryParameters;

import si.luka2.prpo.sportapp.entitete.Uporabnik;
import si.luka2.prpo.sportapp.zrna.UporabnikiZrno;

import java.util.List;

//@Secure


//gre za REST APi endpoint , za interakcijo z uporabniki skozi HTTP requeste
@ApplicationScoped
@Path("uporabniki") //vsi requesti na /uporabnik bojo hendlani s tem klasom
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UporabnikiVir {

    @Context
    private UriInfo uriInfo;

    @Inject
    private UporabnikiZrno uporabnikiZrno; //injectali referenco na uporabniško zrno

    @Operation(description = "Vrne seznam vseh uporabnikov", summary = "dodajanje")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = Uporabnik.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih uporabnikov")}
            )})
    @RolesAllowed("user")
    @GET
    public Response pridobiUporabnike(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        List<Uporabnik> users = uporabnikiZrno.vrniUporabnike();
        Long totalCount = uporabnikiZrno.pridobiUporabnikeCount(null);

        return Response.ok(users, MediaType.APPLICATION_JSON)
                .header("X-Total-Count", totalCount)
                .build();
    }

    @Operation(description = "Vrne enega uporabnik.", summary = "uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En uporabnik",
                    content = @Content(schema = @Schema(implementation = Uporabnik.class, type = SchemaType.OBJECT))
            )})
    @RolesAllowed("user")
    @GET
    @Path("{id}")
    public Response pridobiUporabnika(@PathParam("id") int uporabnikId) {
        Uporabnik uporabnik = uporabnikiZrno.pridobiUporabnika(uporabnikId);
        if(uporabnik == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Uporabnik ni bil najden")
                    .build();
        }
        return Response.ok(uporabnik, MediaType.APPLICATION_JSON).build();
    }
    @Operation(description = "Doda uporabnika.", summary = "Dodajanje")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Uporabnik uspešno dodan",
                    content = @Content(schema = @Schema(implementation = Uporabnik.class, type = SchemaType.OBJECT))),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri dodajanju uporabnika")
    })
    @RolesAllowed("user")
    @POST
    @Path("/dodaj")
    public Response dodajUporabnike(Uporabnik uporabnik) { //argument ki je passan tukaj, se doda cez klic APIja
        Uporabnik novi = uporabnikiZrno.dodajUporabnika(uporabnik); // recimo v postmanu ko mas body POSTa

        if(novi == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Spodletel poskus kreacije uporabnika")
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
                    content = @Content(schema = @Schema(implementation = Uporabnik.class, type = SchemaType.OBJECT))),

            @APIResponse(responseCode = "400",
                    description = "Napaka pri posodabljanju uporabnika")

    })
    @RolesAllowed("user")
    @PATCH
    @Path("/posodobi/{id}")
    public Response posodobiUporabnika(@PathParam("id") int uporabnikId,Uporabnik uporabnik) {

        Uporabnik novi = uporabnikiZrno.posodobiUporabnika(uporabnikId, uporabnik);
        if(novi == null){
            return Response.status(Response.Status.NOT_MODIFIED)
                    .entity("Spodletel poskus posodobitve uporabnika")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(novi)
                .build();
    }


    @Operation(description = "brisanje uporabniki", summary = "brisanje")
    @APIResponses({
            @APIResponse(description = "Uporabnik uspešno izbrisan",
                    responseCode = "204"),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri brisanju uporabnika")

    })
    @RolesAllowed("user")
    @DELETE
    @Path("/odstrani/{id}")
    public Response odstraniUporabnika(@PathParam("id") int uporabnikId) {

        boolean novi = uporabnikiZrno.odstraniUporanbika(uporabnikId);
        if(!novi){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Spodletel poskus brisanja uporabnika")
                    .build();
        }

        return Response.noContent().build();
    }
}

//The UporabnikiVir class acts as a bridge between HTTP requests and the UporabnikiZrno data
//access bean, leveraging UporabnikiZrno methods to perform
//actions on Uporabnik entities.
