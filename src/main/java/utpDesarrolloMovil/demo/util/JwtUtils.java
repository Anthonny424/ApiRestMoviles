package utpDesarrolloMovil.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator; //Usuario que va a generar el token, que solo lo conoce el backend

    public String createToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
        String username = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities() //Obtener las autorizaciones del usuario y separarlas por coma
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username) //El sujeto a quien se le va a generar el token
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date()) //Momento actual cuando se genera el token
                .withExpiresAt(new Date(System.currentTimeMillis()+1800000)) //Momento actual + 30min en milisegundos se va a vencer el token
                .withJWTId(UUID.randomUUID().toString()) //darle un id random al token
                .withNotBefore(new Date(System.currentTimeMillis())) //El token a partir de este momento es valido
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm) //Verifica el token
                    .withIssuer(this.userGenerator)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token); //Devolvemos el JWT decodificado
            return decodedJWT;

        }catch (JWTVerificationException e){
            throw new JWTVerificationException("Token invalido: "+e);
        }
    }

    public String extractUsename(DecodedJWT decodedJWT){ //Recuperar el usuario a quien se le generó el token
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
