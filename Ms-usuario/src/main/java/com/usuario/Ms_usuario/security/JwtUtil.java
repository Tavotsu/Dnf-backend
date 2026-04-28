package com.usuario.Ms_usuario.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // IMPORTANTE: En producción, esta clave debe venir de tu application.yml
    private static final String SECRET_STRING = "EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de duración
                .signWith(key)
                .compact();
    }
}