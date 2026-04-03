package com.dcknotfnd.Ms_mascota;

import com.dcknotfnd.Ms_mascota.controller.MascotaController;
import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(MascotaController.class)
public class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones de Postman

    @MockBean
    private MascotaRepository mascotaRepository; // Simula la base de datos

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON

    // Aquí guardaremos nuestros "datos de prueba"
    private Mascota mascotaPrueba;

    @BeforeEach
    void setUp() {
        // Configuramos los DATOS antes de cada test
        mascotaPrueba = new Mascota();
        mascotaPrueba.setId(1L);
        mascotaPrueba.setNombre("Firulais");
        mascotaPrueba.setEspecie("Perro");
        mascotaPrueba.setRaza("Quiltro");
        mascotaPrueba.setColor("Negro");
        mascotaPrueba.setTamano("Mediano");
        mascotaPrueba.setUsuarioId(10L);
    }

    @Test
    void testRegistrarMascota() throws Exception {
        // 1. Preparamos el comportamiento: Cuando el repository guarde, devolverá nuestra mascotaPrueba
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaPrueba);

        // 2. Hacemos el POST simulado
        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaPrueba))) // Enviamos los datos en JSON

                // 3. Verificamos que responda 200 OK y que los datos sean correctos
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Firulais"))
                .andExpect(jsonPath("$.especie").value("Perro"));
    }

    @Test
    void testObtenerTodasLasMascotas() throws Exception {
        // 1. Preparamos una lista de datos
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        Mockito.when(mascotaRepository.findAll()).thenReturn(listaDatos);

        // 2. Hacemos el GET simulado
        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // Verificamos que traiga 1 mascota
                .andExpect(jsonPath("$[0].nombre").value("Firulais")); // Verificamos el nombre
    }

    @Test
    void testObtenerMascotasPorUsuario() throws Exception {
        // 1. Preparamos los datos
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        Mockito.when(mascotaRepository.findByUsuarioId(10L)).thenReturn(listaDatos);

        // 2. Hacemos el GET simulado pasando el ID del usuario en la URL
        mockMvc.perform(get("/api/mascotas/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(10));
    }
}