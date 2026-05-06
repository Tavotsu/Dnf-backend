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
    private MockMvc mockMvc;

    @MockBean
    private MascotaRepository mascotaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Mascota mascotaPrueba;

    @BeforeEach
    void setUp() {
        mascotaPrueba = new Mascota();
        mascotaPrueba.setId(1L);
        mascotaPrueba.setName("Firulais"); // Compilación: ajustado a name
        mascotaPrueba.setType("Perro");    // Compilación: ajustado a type
        mascotaPrueba.setBreed("Quiltro"); // Compilación: ajustado a breed
        mascotaPrueba.setColor("Negro");
        // Compilación: se eliminó setTamano ya que no existe en el modelo actual
        mascotaPrueba.setUsuarioId(10L);
    }

    @Test
    void testRegistrarMascota() throws Exception {
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaPrueba);

        mockMvc.perform(post("/api/pets/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaPrueba)))

                // 3. Verificamos que responda 201 Created y que el JSON de respuesta sea el correcto
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Reporte creado exitosamente"));
    }

    @Test
    void testObtenerTodasLasMascotas() throws Exception {
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);

        // Prueba: ajustado para mockear el nuevo método buscarConFiltros en lugar de findAll
        Mockito.when(mascotaRepository.buscarConFiltros(any(), any(), any())).thenReturn(listaDatos);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // Verificamos que traiga 1 mascota
                .andExpect(jsonPath("$[0].name").value("Firulais")); // Prueba: Verificamos el name
    }

    @Test
    void testObtenerMascotaPorId() throws Exception {
        Mockito.when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaPrueba));

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("lost"));
    }
}