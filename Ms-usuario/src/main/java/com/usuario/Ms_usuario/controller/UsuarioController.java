package com.usuario.Ms_usuario.controller;

import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "API de Usuarios", description = "Controlador para gestionar el registro y listado de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Registrar un nuevo usuario", description = "Guarda un usuario en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<Usuario> save(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

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
}