package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "API de Mascotas", description = "Controlador para gestionar las mascotas de los usuarios")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    // post
    @Operation(summary = "Registrar una nueva mascota", description = "Guarda una nueva mascota en la base de datos asociada a un ID de usuario.")
    @ApiResponse(responseCode = "200", description = "Mascota creada exitosamente")
    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaRepository.save(mascota);
        return ResponseEntity.ok(nuevaMascota);
    }

    // get
    @Operation(summary = "Listar todas las mascotas", description = "Obtiene una lista completa de todas las mascotas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Mascota> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return mascotaRepository.findByUsuarioId(usuarioId);
    }
}