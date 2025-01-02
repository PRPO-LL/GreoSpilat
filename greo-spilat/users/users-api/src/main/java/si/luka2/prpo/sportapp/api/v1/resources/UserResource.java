package si.luka2.prpo.sportapp.api.v1.resources;

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

import si.luka2.prpo.sportapp.entities.User;
import si.luka2.prpo.sportapp.beans.UserBean;

import java.util.List;
import java.util.logging.Logger;

//@Secure


//gre za REST APi endpoint , za interakcijo z uporabniki skozi HTTP requeste
@ApplicationScoped
@Path("users") //vsi requesti na /uporabnik bojo hendlani s tem klasom
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Context
    private UriInfo uriInfo;

    @Inject
    private UserBean userBean; //injectali referenco na uporabniško zrno

    @Operation(description = "Vrne seznam vseh uporabnikov", summary = "dodajanje")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih uporabnikov")}
            )})
    @RolesAllowed("user")
    @GET
    public Response getUsers(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        List<User> users = userBean.getUsers();
        Long totalCount = userBean.getUserCount(null);

        return Response.ok(users, MediaType.APPLICATION_JSON)
                .header("X-Total-Count", totalCount)
                .build();
    }

    @Operation(description = "Vrne enega uporabnik.", summary = "uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En uporabnik",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.OBJECT))
            )})
    @RolesAllowed("user")
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int uporabnikId) {
        User user = userBean.getUser(uporabnikId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Uporabnik ni bil najden")
                    .build();
        }
        return Response.ok(user, MediaType.APPLICATION_JSON).build();
    }
    @Operation(description = "Doda uporabnika.", summary = "Dodajanje")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Uporabnik uspešno dodan",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.OBJECT))),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri dodajanju uporabnika")
    })
    @RolesAllowed("user")
    @POST
    @Path("/add")
    public Response addUser(User user) { //argument ki je passan tukaj, se doda cez klic APIja


        if (user.getUsername() == null || user.getUser_id() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Manjkajoči podatki v zahtevi. Preverite uporabniško ime in geslo.")
                    .build();
        }
        User novi = userBean.addUser(user); // recimo v postmanu ko mas body POSTa

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
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.OBJECT))),

            @APIResponse(responseCode = "400",
                    description = "Napaka pri posodabljanju uporabnika")

    })
    @RolesAllowed("user")
    @PATCH
    @Path("/update/{id}")
    public Response updateUser(@PathParam("id") int uporabnikId, User user) {

        User novi = userBean.updateUser(uporabnikId, user);
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
    @Path("/delete/{id}")
    public Response deleteUser(@PathParam("id") int uporabnikId) {

        boolean novi = userBean.deleteUser(uporabnikId);
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
