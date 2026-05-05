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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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
        // Configuramos los DATOS usando los nuevos atributos en inglés
        mascotaPrueba = new Mascota();
        mascotaPrueba.setId(1L);
        mascotaPrueba.setName("Firulais");
        mascotaPrueba.setType("Perro");
        mascotaPrueba.setBreed("Quiltro");
        mascotaPrueba.setGender("Macho");
        mascotaPrueba.setStatus("lost");
        mascotaPrueba.setLocation("Puerto Montt");
    }

    @Test
    void testRegistrarMascota() throws Exception {
        // 1. Preparamos el comportamiento
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaPrueba);

        // 2. Hacemos el POST simulado a la nueva ruta /api/pets/report
        mockMvc.perform(post("/api/pets/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaPrueba)))

                // 3. Verificamos que responda 200 OK y que los nuevos JSON keys coincidan
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.type").value("Perro"));
    }

    @Test
    void testObtenerTodasLasMascotas() throws Exception {
        // 1. Preparamos una lista de datos
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        
        // Simulamos la nueva función del repositorio que usa filtros (pasando null simulamos que no hay filtros)
        Mockito.when(mascotaRepository.buscarConFiltros(isNull(), isNull(), isNull())).thenReturn(listaDatos);

        // 2. Hacemos el GET simulado a /api/pets
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // Verificamos que traiga 1 mascota
                .andExpect(jsonPath("$[0].name").value("Firulais")); // Verificamos el nombre
    }

    @Test
    void testObtenerMascotaPorId() throws Exception {
        // 1. Preparamos los datos
        Mockito.when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaPrueba));

        // 2. Hacemos el GET simulado al endpoint específico de ID
        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("lost"));
    }
}