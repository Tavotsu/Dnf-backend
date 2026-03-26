package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaRepository.save(mascota);
        return ResponseEntity.ok(nuevaMascota);
    }

    @GetMapping
    public List<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Mascota> obtenerPorUsuario(@PathVariable Long usuarioId) {
        // Actualizado para usar el nuevo método
        return mascotaRepository.findByUsuarioId(usuarioId);
    }
}
