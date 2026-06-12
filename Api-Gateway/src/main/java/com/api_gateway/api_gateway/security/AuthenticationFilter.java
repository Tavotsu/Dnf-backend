package com.api_gateway.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro de autenticación del Gateway.
 * Valida tokens JWT para todas las rutas excepto las rutas públicas.
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Lista de rutas públicas que no requieren autenticación
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/api/usuarios/login",
        "/api/usuarios",
        "/swagger-ui",
        "/v3/api-docs",
        "/actuator"
    );

    private static final List<String> PUBLIC_METHODS = Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1. Verificar si es una ruta pública
        if (isPublicRoute(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer cabecera Authorization
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Gateway: AuthHeader: " + authHeader);

        // 3. Validar si existe y tiene el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Gateway: Token missing or format invalid");
            sendUnauthorizedError(response, "Token faltante o inválido");
            return;
        }

        // 4. Extraer token y validarlo
        final String token = authHeader.substring(7);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                System.out.println("Gateway: Token is invalid: " + token);
                sendUnauthorizedError(response, "Token expirado o inválido");
                return;
            }
        } catch (Exception e) {
            System.out.println("Gateway: Exception validating JWT: " + e.getMessage());
            e.printStackTrace();
            sendUnauthorizedError(response, "Error validando token");
            return;
        }

        // 5. Token válido, continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si la ruta es pública.
     * Una ruta es pública si coincide con las rutas permitidas.
     * 
     * @param path Ruta de la solicitud
     * @param method Método HTTP
     * @return true si la ruta es pública, false en caso contrario
     */
    private boolean isPublicRoute(String path, String method) {
        return PUBLIC_PATHS.stream()
            .anyMatch(path::equals);
    }

    /**
     * Envía una respuesta de error no autorizado.
     * 
     * @param response HttpServletResponse
     * @param message Mensaje de error
     * @throws IOException Si ocurre un error de I/O
     */
    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}

