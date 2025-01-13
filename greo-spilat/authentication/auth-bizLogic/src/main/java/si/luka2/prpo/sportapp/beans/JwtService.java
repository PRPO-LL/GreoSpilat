package si.luka2.prpo.sportapp.beans;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import si.luka2.prpo.sportapp.entities.UserAuth;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

@ApplicationScoped
public class JwtService {
    private static final String SECRET_KEY = "secret";
    private static final long EXPIRATION_TIME = 20 * 60 * 1000; //20 minut v milisekundah

    public static String generateToken(UserAuth user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        if(user.getUsername().equals("admin")) {
            return JWT.create()
                    .withClaim("id", user.getId())
                    .withClaim("username", user.getId())
                    .withClaim("roles", "admin")
                    .withExpiresAt(expiryDate)
                    .sign(algorithm);
        }
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getId())
                .withClaim("roles", "user")
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    public static int validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(token);
        if(jwt.getExpiresAt().after(new Date())){
            return jwt.getClaim("id").asInt();
        }
        return -1;
    }
    public static Date expiresAt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(token);
        return jwt.getExpiresAt();
    }
    public static Date now(){
        return new Date();
    }
}

