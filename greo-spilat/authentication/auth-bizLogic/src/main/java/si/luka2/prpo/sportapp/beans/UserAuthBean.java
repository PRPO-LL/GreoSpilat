package si.luka2.prpo.sportapp.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.io.entity.StringEntity;
import si.luka2.prpo.sportapp.DTOs.RegisterUserDTO;
import si.luka2.prpo.sportapp.entities.UserAuth;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.logging.Logger;

@RequestScoped
public class UserAuthBean {
    private Logger log = Logger.getLogger(UserAuthBean.class.getName());
    private CloseableHttpClient httpClient;
    private String basePath;
    private ObjectMapper objectMapper;
    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UserAuthBean.class.getSimpleName());
        httpClient = HttpClientBuilder.create().build();
        basePath = "http://localhost:8084/v1/";
        // inicializacija virov
        objectMapper = new ObjectMapper();
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UserAuthBean.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "greo-spilat-jpa")
    private EntityManager em;

    public UserAuth getUser(int uporabnikId) {

        return em.createNamedQuery("User.getById", UserAuth.class)
                .setParameter("id", uporabnikId)
                .getSingleResult();
    }

    public UserAuth getUser(String username) {

        return em.createNamedQuery("User.getByUsername", UserAuth.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    //ustvari userja, mu hasha password, ustvari userja v users tabeli!
    @Transactional
    public UserAuth createUser(RegisterUserDTO user) {
        if(user.getUsername()== null || user.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);
        Hash hash = Password.hash(user.getPassword())
                .with(bcrypt);

        UserAuth newUser = new UserAuth();
        newUser.setUsername(user.getUsername());
        newUser.setHashPassword(hash.getResult());
        em.persist(newUser);

//        try{
//            HttpPost httpPost = new HttpPost(basePath + "users/add");
//            httpPost.setHeader("Content-Type", "application/json");
//            ObjectNode userJson = objectMapper.createObjectNode();
//            userJson.put("username", user.getUsername());
//            userJson.put("user_id", newUser.getId());
//
//            String payload = userJson.toString();
//
//            httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
//            HttpResponse httpResponse = httpClient.executeOpen(null, httpPost, null);
//
//            int status = httpResponse.getCode();
//
//            if(status >= 200 && status < 300) {
//                return newUser;
//            }
//            else{
//                String msg = "Remote server " + basePath + "responded with status " + status;
//                log.info(msg);
//                throw new InternalServerErrorException(msg);
//            }
//        } catch (IOException e) {
//            String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
//            log.info(msg);
//            throw new InternalServerErrorException(e);
//        }
        return newUser;
    }
    //tukaj treba dodat vračanje jwt tokena
//    @Transactional
    public boolean login(RegisterUserDTO user1) {

        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

        return Password.check(user1.getPassword(), getUser(user1.getUsername()).getHashPassword())
                .with(bcrypt);
    }
    public void remove(RegisterUserDTO user) {
        em.remove(getUser(user.getUsername()));
    }

    @Transactional
    public boolean deleteUser(RegisterUserDTO user1) {
        //toti if login pol menjaj ker bo login vracal jwt token
        if(login(user1)) {
            UserAuth user = getUser(user1.getUsername());
            em.remove(user);
//            try{
//                HttpDelete httpDelete = new HttpDelete(basePath + "users/delete/" + user.getId());
//                HttpResponse httpResponse = httpClient.executeOpen(null, httpDelete, null);
//
//                int status = httpResponse.getCode();
//
//                if(status == 204){
//                    return true;
//                }
//                else if(status == 404){
//                    String msg = "Remote server " + basePath + "responded with status " + status;
//                    log.info(msg);
//                    throw new InternalServerErrorException(msg);
//                }
//                else{
//                    throw new HttpResponseException(status, httpResponse.getReasonPhrase());
//                }
//            } catch (IOException e) {
//                String msg = e.getClass().getSimpleName() + " occured " + e.getMessage();
//                log.info(msg);
//                throw new InternalServerErrorException(e);
//            }
            return true;
        }
        return false;

    }


}
