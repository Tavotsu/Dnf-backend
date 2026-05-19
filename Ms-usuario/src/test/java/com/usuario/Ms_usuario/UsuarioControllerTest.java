package com.usuario.Ms_usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.Ms_usuario.controller.UsuarioController;
import com.usuario.Ms_usuario.dto.AuthRequest;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import com.usuario.Ms_usuario.security.JwtFilter;
import com.usuario.Ms_usuario.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para aislar el test del controlador
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1L);
        usuarioPrueba.setName("Juan Perez");
        usuarioPrueba.setEmail("juan@gmail.com");
        usuarioPrueba.setPassword("secreta123");
        usuarioPrueba.setRol("ciudadano");
    }

    @Test
    void testRegistrarUsuario() throws Exception {
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("passwordEncriptada123");
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPrueba);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Perez"))
                .andExpect(jsonPath("$.rol").value("ciudadano"));
    }

    @Test
    void testObtenerTodosLosUsuarios() throws Exception {
        List<Usuario> listaUsuarios = Arrays.asList(usuarioPrueba);
        Mockito.when(usuarioRepository.findAll()).thenReturn(listaUsuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan Perez"));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioRepository).deleteById(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginExitoso() throws Exception {
        AuthRequest authRequest = new AuthRequest("juan@gmail.com", "secreta123");

        Mockito.when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioPrueba));
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(anyString())).thenReturn("tokenGenerado123");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tokenGenerado123"))
                .andExpect(jsonPath("$.user.name").value("Juan Perez"));
    }

    @Test
    void testLoginCredencialesInvalidas() throws Exception {
        AuthRequest authRequest = new AuthRequest("juan@gmail.com", "claveIncorrecta");

        Mockito.when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioPrueba));
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginUsuarioNoEncontradoLanzaExcepcion() {
        AuthRequest authRequest = new AuthRequest("noexiste@gmail.com", "secreta123");

        Mockito.when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Usamos assertThrows de JUnit para atrapar la excepción que rompe la petición
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/api/usuarios/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)));
        });

        // MockMvc envuelve nuestra RuntimeException dentro de una NestedServletException
        // Así que verificamos la causa original (getCause)
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Usuario no encontrado", exception.getCause().getMessage());
    }
}