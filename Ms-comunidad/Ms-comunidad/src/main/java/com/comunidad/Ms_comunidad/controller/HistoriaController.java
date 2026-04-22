package com.comunidad.Ms_comunidad.controller;

import com.comunidad.Ms_comunidad.model.Historia;
import com.comunidad.Ms_comunidad.repository.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/success-stories")
public class HistoriaController {

    @Autowired
    private HistoriaRepository historiaRepository;

    // Endpoint para que el Frontend liste las historias
    @GetMapping
    public ResponseEntity<List<Historia>> obtenerHistorias() {
        return ResponseEntity.ok(historiaRepository.findAll());
    }

    // Endpoint para guardar una nueva historia de éxito
    @PostMapping
    public ResponseEntity<Historia> crearHistoria(@RequestBody Historia historia) {
        Historia nuevaHistoria = historiaRepository.save(historia);
        return ResponseEntity.ok(nuevaHistoria);
    }
}