package com.bruno.movieflix.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bruno.movieflix.custom_exceptions.MyUnauthorizedException;
import com.bruno.movieflix.entities.User;
import com.bruno.movieflix.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@Component
public class MyTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final String issuer = "auth_api";

    private Instant expires_in;

    private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);


    public String generateToken(User user) {

        try {
            this.expires_in = genExpirationDate();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            logger.info(algorithm.toString());
            String token = JWT.create()
                    .withIssuer(this.issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpiresIn())
                    .sign(algorithm);

            return token;

        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }


    public String validateJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .build()
                    .verify(token)
                    .getSubject();


        } catch (JWTVerificationException e) {
            throw new MyUnauthorizedException("Token inválido ou expirado");
            //return "";
        }
    }


    private Instant genExpirationDate() {
        //Duração do token
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
        //return LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(30).toInstant(ZoneOffset.UTC);
    }


    public Instant getExpiresIn() {
        return expires_in;
    }

}
