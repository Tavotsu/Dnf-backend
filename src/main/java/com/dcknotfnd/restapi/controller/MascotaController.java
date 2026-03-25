package com.dcknotfnd.restapi.controller;
import com.dcknotfnd.restapi.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dcknotfnd.restapi.model.Mascota;
#a
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
        return mascotaRepository.findByUsuario(usuarioId);
    }
}
