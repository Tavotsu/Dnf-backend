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
import com.usuario.Ms_usuario.dto.AuthRequest;
import com.usuario.Ms_usuario.dto.AuthResponse;
import com.usuario.Ms_usuario.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


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

    @Operation(summary = "Registrar un nuevo usuario", description = "Guarda un usuario en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
     
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }
    /*
    UsuarioController se encarga de resgitrar y gestionar los usuarios que intenten 
    logearse en la aplicacion o pagina web ademas que al tener implementado JWT, 
    se encarga de generar el token de autenticacion para los usuarios que intenten logearse.

    @param usuario: Objeto que contiene la información del usuario a registrar (nombre, email, contraseña)
    @return: El usuario creado con su información registrada en la base de datos
     
    */

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
        
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        
        if (passwordEncoder.matches(request.password(), usuario.getPassword())) {
            
            String token = jwtUtil.generateToken(usuario.getEmail());

            
            return ResponseEntity.ok(new AuthResponse(token, usuario));
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

/*
aqui se lista los usuarios con una ID, se elimina un usuario por su ID 
y se implementa el login de usuario, que al momento de logearse,
 se genera un token JWT para la autenticacion del usuario en las siguientes peticiones.

@param id: ID del usuario a eliminar
@param request: Objeto que contiene la información de autenticación (email y contraseña)
@return: Respuesta indicando si el login fue exitoso con el token JWT generado o si las credenciales son inválidas
}

*/
}
