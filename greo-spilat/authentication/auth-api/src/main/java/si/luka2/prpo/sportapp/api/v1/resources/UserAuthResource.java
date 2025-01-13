package si.luka2.prpo.sportapp.api.v1.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.luka2.prpo.sportapp.DTOs.RegisterUserDTO;
import si.luka2.prpo.sportapp.beans.JwtService;
import si.luka2.prpo.sportapp.beans.UserAuthBean;
import si.luka2.prpo.sportapp.entities.UserAuth;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAuthResource {
    private Logger log = Logger.getLogger(UserAuthResource.class.getName());
    private CloseableHttpClient httpClient;
    private String basePath;
    private ObjectMapper objectMapper;
    @Context
    private UriInfo uriInfo;

    @Inject
    private UserAuthBean userAuthBean;


    @Operation(description = "Vrne enega uporabnik.", summary = "uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En uporabnik",
                    content = @Content(schema = @Schema(implementation = UserAuth.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
            description = "Uporabniki niso bili najdeni"
    )
    })
    @RolesAllowed("user")
    @GET
    @Path("all")
    public Response getUsers() {
        List<UserAuth> list = userAuthBean.getUsers();
        if(list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Uporabniki niso bili najdeni")
                    .build();
        }
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @Operation(description = "Vrne enega uporabnik.", summary = "uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "En uporabnik",
                    content = @Content(schema = @Schema(implementation = UserAuth.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description = "Uporabnik ni bil najden"
            )

    })
    @RolesAllowed("user")
    @GET
    @Path("{username}")
    public Response getUser(@PathParam("username") String username) {
        UserAuth user = userAuthBean.getUser(username);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Uporabnik ni bil najden")
                    .build();
        }
        return Response.ok(user, MediaType.APPLICATION_JSON).build();
    }



    @Operation(description = "Registrira uporabnika.", summary = "Registriranje")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Uporabnik uspešno Registriran",
                    content = @Content(schema = @Schema(implementation = UserAuth.class, type = SchemaType.OBJECT))),
            @APIResponse(responseCode = "400",
                    description = "Napaka pri registriranju uporabnika")
    })
    @RolesAllowed("user")
    @POST
    @Path("/register")
    public Response addUser(RegisterUserDTO user) { //argument ki je passan tukaj, se doda cez klic APIja
        httpClient = HttpClientBuilder.create().build();
        basePath = "http://user-service:8084/v1/";
        // inicializacija virov
        objectMapper = new ObjectMapper();

        if (user.getUsername() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Manjkajoči podatki v zahtevi. Preverite uporabniško ime in geslo")
                    .build();
        }
        UserAuth novi = userAuthBean.createUser(user); // recimo v postmanu ko mas body POSTa
//
//        if(novi == null){
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Spodletel poskus kreacije uporabnika")
//                    .build();
//        }

        try{
            HttpPost httpPost = new HttpPost(basePath + "users/add");
            httpPost.setHeader("Content-Type", "application/json");
            ObjectNode userJson = objectMapper.createObjectNode();
            userJson.put("username", novi.getUsername());
            userJson.put("user_id", novi.getId());

            String payload = userJson.toString();

            httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
            HttpResponse httpResponse = httpClient.executeOpen(null, httpPost, null);

            int status = httpResponse.getCode();

            if(status >= 200 && status < 300) {

                return Response.status(Response.Status.CREATED)
                        .entity(novi)
                        .build();
            }
            else{
                userAuthBean.remove(user);
                String msg = "Remote server " + basePath + "responded with status " + status;
                log.info(msg);
                throw new InternalServerErrorException(msg);
            }
        } catch (IOException e) {
            String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
            log.info(msg);
            throw new InternalServerErrorException(e);
        }

    }

    @Operation(description = "Login", summary = "login")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "login",
                    content = @Content(schema = @Schema(implementation = UserAuth.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                description = "Spodletel poskus logiranja uporabnika"
            )
    })
    @RolesAllowed("user")
    @POST
    @Path("/login")
    public Response login(RegisterUserDTO user) {
        boolean login = userAuthBean.login(user);
        if(!login) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Login spodletel")
                    .build();
        }
        UserAuth novi = userAuthBean.getUser(user.getUsername());
        String token = JwtService.generateToken(novi);
        return Response.ok(user, MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .build();
    }

    @Operation(description = "brisanje uporabniki", summary = "brisanje")
    @APIResponses({
            @APIResponse(description = "Uporabnik uspešno izbrisan",
                    responseCode = "204"),
            @APIResponse(responseCode = "404",
                    description = "Spodletel poskus brisanja uporabnika")

    })
    @RolesAllowed("user")
    @DELETE
    @Path("/delete")
    public Response deleteUser(@Context HttpHeaders headers,RegisterUserDTO user) {
        String header = headers.getHeaderString("Authorization");
        httpClient = HttpClientBuilder.create().build();
        basePath = "http://user-service:8084/v1/";
        // inicializacija virov
        objectMapper = new ObjectMapper();
        UserAuth userAuth = userAuthBean.getUser(user.getUsername());
        try{
            HttpDelete httpDelete = new HttpDelete(basePath + "users/delete/" + userAuth.getId());
            httpDelete.addHeader("Authorization", header);
            HttpResponse httpResponse = httpClient.executeOpen(null, httpDelete, null);

            int status = httpResponse.getCode();
            if(status == 204){
                if(userAuthBean.deleteUser(user))
                    return Response.noContent().build();
                else{
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("Spodletel poskus brisanja uporabnika")
                                .build();
                }

            }
            else if(status == 404){
                String msg = "Remote server " + basePath + "responded with status " + status;
                log.info(msg);
                throw new InternalServerErrorException(msg);
            }
            else{
                throw new HttpResponseException(status, httpResponse.getReasonPhrase());
            }
        } catch (IOException e) {
            String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
            log.info(msg);
            throw new InternalServerErrorException(e);
        }
    }
    @Operation(description = "Validacija tokena", summary = "validacija")
    @APIResponses({
            @APIResponse(description = "Token je validen",
                    responseCode = "200"),
            @APIResponse(description = "Auth header manjka",
                    responseCode = "400"),
            @APIResponse(description = "Token je potekel",
                    responseCode = "401"),

    })
    @RolesAllowed("user")
    @POST
    @Path("/validate")
    public Response validateToken(@Context HttpHeaders headers) {
        String header = headers.getHeaderString("Authorization");
        if(header == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Auth header manjka")
                    .build();
        }
        int id = JwtService.validateToken(header);
        if(id < 0){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token je potekel")
                    .build();
        }
        else{
            Response.ResponseBuilder builder = Response.ok("Token je validen");
            builder.header("Id", id);
            builder.header("Authorization", header);
            return builder.build();
        }
    }


}
