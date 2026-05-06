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
        mascotaPrueba.setName("Firulais");
        mascotaPrueba.setType("Perro");
        mascotaPrueba.setBreed("Quiltro");
        mascotaPrueba.setGender("Macho");
        mascotaPrueba.setStatus("lost");
        mascotaPrueba.setLocation("Puerto Montt");
        mascotaPrueba.setLatitude(-41.4693);
        mascotaPrueba.setLongitude(-72.9423);
    }

    @Test
    void testRegistrarMascota() throws Exception {
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaPrueba);

        mockMvc.perform(post("/api/pets/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.type").value("Perro"));
    }

    @Test
    void testObtenerTodasLasMascotas() throws Exception {
        List<Mascota> listaDatos = Arrays.asList(mascotaPrueba);
        
        Mockito.when(mascotaRepository.buscarConFiltros(isNull(), isNull(), isNull())).thenReturn(listaDatos);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Firulais"));
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