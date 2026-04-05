package com.dcknotfnd.ms_coincidencias;

import com.dcknotfnd.ms_coincidencias.client.MascotaClient;
import com.dcknotfnd.ms_coincidencias.controller.CoincidenciaController;
import com.dcknotfnd.ms_coincidencias.model.Coincidencia;
import com.dcknotfnd.ms_coincidencias.repository.CoincidenciaRepository;
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

@WebMvcTest(CoincidenciaController.class)
public class CoincidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoincidenciaRepository coincidenciaRepository;


    @MockBean
    private MascotaClient mascotaClient;

    @Autowired
    private ObjectMapper objectMapper;

    private Coincidencia coincidenciaPrueba;

    @BeforeEach
    void setUp() {
        coincidenciaPrueba = new Coincidencia(1L, "Perro", "Blanco", "PENDIENTE");
    }

    @Test
    void testCrearCoincidencia() throws Exception {
        Mockito.when(coincidenciaRepository.save(any(Coincidencia.class))).thenReturn(coincidenciaPrueba);

        mockMvc.perform(post("/api/coincidencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coincidenciaPrueba)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especieBuscada").value("Perro"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testListarCoincidencias() throws Exception {
        List<Coincidencia> lista = Arrays.asList(coincidenciaPrueba);
        Mockito.when(coincidenciaRepository.findAll()).thenReturn(lista);

        mockMvc.perform(get("/api/coincidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].colorBuscado").value("Blanco"));
    }
}
