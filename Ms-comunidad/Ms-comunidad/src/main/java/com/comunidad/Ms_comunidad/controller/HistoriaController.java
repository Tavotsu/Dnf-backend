package com.comunidad.Ms_comunidad.controller;

import com.comunidad.Ms_comunidad.model.Historia;
import com.comunidad.Ms_comunidad.repository.HistoriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/success-stories")
public class HistoriaController {

    @Autowired
    private HistoriaRepository historiaRepository;

    @GetMapping
    public ResponseEntity<List<Historia>> obtenerHistorias() {
        return ResponseEntity.ok(historiaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Historia> crearHistoria(@Valid @RequestBody Historia historia) {
        Historia nuevaHistoria = historiaRepository.save(historia);
        return ResponseEntity.ok(nuevaHistoria);
    }
}