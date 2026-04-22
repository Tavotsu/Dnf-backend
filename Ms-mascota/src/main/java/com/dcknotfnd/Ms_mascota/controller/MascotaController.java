package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets") 
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    // Endpoint 1: Listar mascotas (con filtros opcionales del frontend)
    // Se consume así: GET /api/pets?status=lost&type=Perro
    @GetMapping
    public ResponseEntity<List<Mascota>> obtenerMascotas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String query) {
        
        List<Mascota> resultados = mascotaRepository.buscarConFiltros(status, type, query);
        return ResponseEntity.ok(resultados);
    }

    // Endpoint 2: Reportar una nueva mascota
    // Se consume así: POST /api/pets/report
    @PostMapping("/report")
    public ResponseEntity<Mascota> reportarMascota(@RequestBody Mascota mascota) {
        // Aquí guardamos directamente la mascota que nos envía el frontend
        Mascota nuevaMascota = mascotaRepository.save(mascota);
        return ResponseEntity.ok(nuevaMascota);
    }
    
    // Endpoint 3: Obtener mascota por ID (Opcional pero muy útil para el frontend)
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Long id) {
        return mascotaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}