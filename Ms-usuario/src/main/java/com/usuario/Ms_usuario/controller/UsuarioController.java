package com.usuario.Ms_usuario.controller;

import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.usuario.Ms_usuario.dto.AuthRequest;
import com.usuario.Ms_usuario.dto.AuthResponse;
import com.usuario.Ms_usuario.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "API de Usuarios", description = "Controlador para gestionar el registro y listado de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

   // post
    @Operation(summary = "Registrar un nuevo usuario", description = "Guarda un usuario en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Encriptar la contraseña antes de guardarla en la BD
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

   // get
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista completa de todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // 1. Buscar usuario por email (necesitarás agregar findByEmail en tu repositorio)
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar contraseña
        if (passwordEncoder.matches(request.password(), usuario.getPassword())) {
            // 3. Generar Token
            String token = jwtUtil.generateToken(usuario.getEmail());

            // 4. Retornar la respuesta tal cual la pide el Frontend
            return ResponseEntity.ok(new AuthResponse(token, usuario));
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
