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

    @GetMapping
    public ResponseEntity<List<Historia>> obtenerHistorias() {
        return ResponseEntity.ok(historiaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Historia> crearHistoria(@RequestBody Historia historia) {
        Historia nuevaHistoria = historiaRepository.save(historia);
        return ResponseEntity.ok(nuevaHistoria);
    }
}
/*
En historiaController se define la gestion de historias, como obtener las historias registradas
y crear una nueva historia de exito, que se muestra en el frontend.

@param historia: Objeto que contiene la información de la historia a crear (nombre del dueño, nombre de la mascota, historia de exito)
@return: La historia creada con su información registrada en la base de datos
*/