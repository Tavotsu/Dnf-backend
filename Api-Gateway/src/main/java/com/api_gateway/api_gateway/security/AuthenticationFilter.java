package com.api_gateway.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1. Rutas públicas que no requieren token
        if (isPublicRoute(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer cabecera Authorization
        final String authHeader = request.getHeader("Authorization");

        // 3. Validar si existe y tiene el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token faltante o invalido");
            return;
        }

        // 4. Extraer token y validarlo
        final String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token expirado o invalido");
            return;
        }

        // 5. Token válido, continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    private boolean isPublicRoute(String path, String method) {
        // Permitir POST /api/usuarios/login
        if (path.equals("/api/usuarios/login") && method.equals("POST")) {
            return true;
        }
        
        // Permitir POST /api/usuarios (Registro)
        if (path.equals("/api/usuarios") && method.equals("POST")) {
            return true;
        }

        // Permitir acceso a Swagger UI en todos los microservicios
        if (path.contains("/swagger-ui") || path.contains("/v3/api-docs")) {
            return true;
        }

        return false;
    }
}
