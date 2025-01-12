package dev.erickystn.forumhub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.erickystn.forumhub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {


    // pega a chave do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration-hour}")
    private Integer hoursExpiration;


    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Forum Hub") // app que gerou os jwt
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(geraExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Não foi possível gerar um token: " + exception);
        }
    }

    public String obterSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Forum Hub")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token expirado ou inválido.");
        }
    }

    private Instant geraExpiracao() {
        //
        return LocalDateTime.now().plusHours(hoursExpiration).toInstant(ZoneOffset.of("-03:00"));
    }
}
