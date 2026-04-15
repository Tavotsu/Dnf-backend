package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "API de Mascotas")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Operation(summary = "Registrar una nueva mascota")
    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@Valid @RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaRepository.save(mascota);
        return ResponseEntity.ok(nuevaMascota);
    }

    @Operation(summary = "Listar todas las mascotas")
    @GetMapping
    public List<Mascota> obtenerTodasLasMascotas() {
        return mascotaRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMascota(@PathVariable Long id) {
        mascotaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}