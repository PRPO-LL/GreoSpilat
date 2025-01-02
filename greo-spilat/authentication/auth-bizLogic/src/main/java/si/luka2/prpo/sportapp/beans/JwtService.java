package si.luka2.prpo.sportapp.beans;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import si.luka2.prpo.sportapp.entities.UserAuth;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {
    private static final String SECRET_KEY = "secret";

    public static String generateToken(UserAuth user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        if(user.getUsername().equals("admin")) {
            return JWT.create()
                    .withClaim("id", user.getId())
                    .withClaim("username", user.getId())
                    .withClaim("roles", "admin")
                    .sign(algorithm);
        }
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getId())
                .withClaim("roles", "user")
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(token);

        return false;
    }
}

