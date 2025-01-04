package si.luka2.prpo.sportapp.beans;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {
    private static final String SECRET_KEY = "secret";


    public static int validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(token);

        return jwt.getClaim("id").asInt();
    }
}

