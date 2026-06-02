package com.api_gateway.api_gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // Misma clave secreta que en el Ms-usuario
    private static final String SECRET_STRING = "EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Extraer el email (subject) del token
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validar si el token es correcto y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Si la firma es inválida, expiró o está mal formado
            return false;
        }
    }
}
