package com.usuario.Ms_usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.Ms_usuario.controller.UsuarioController;
import com.usuario.Ms_usuario.model.Usuario;
import com.usuario.Ms_usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();
        usuarioPrueba.setId(1L);
        usuarioPrueba.setNombre("Juan Perez");
        usuarioPrueba.setEmail("juan@gmail.com");
        usuarioPrueba.setPassword("secreta123");
        usuarioPrueba.setRol("ciudadano");
    }

    @Test
    void testRegistrarUsuario() throws Exception {
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPrueba);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.rol").value("ciudadano")); // Verificamos el rol extra
    }

    @Test
    void testObtenerTodosLosUsuarios() throws Exception {
        List<Usuario> listaUsuarios = Arrays.asList(usuarioPrueba);
        Mockito.when(usuarioRepository.findAll()).thenReturn(listaUsuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }
}