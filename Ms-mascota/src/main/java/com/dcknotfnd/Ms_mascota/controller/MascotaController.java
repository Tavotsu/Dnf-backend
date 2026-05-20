package com.dcknotfnd.Ms_mascota.controller;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import com.dcknotfnd.Ms_mascota.repository.MascotaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "API de Mascotas", description = "Controlador para gestionar reportes y búsquedas de mascotas")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Operation(summary = "Listar Mascotas con Filtros", description = "Obtiene la lista de mascotas aplicando filtros opcionales del mapa.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Mascota>> obtenerMascotas(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String query) {

        // Convertimos los nulos a strings vacíos para que PostgreSQL no se queje
        String safeStatus = (status != null) ? status : "";
        String safeType = (type != null) ? type : "";
        String safeQuery = (query != null) ? query : "";

        List<Mascota> mascotas = mascotaRepository.buscarConFiltros(safeStatus, safeType, safeQuery);
        return ResponseEntity.ok(mascotas);
    }
    @Operation(summary = "Reportar una mascota", description = "Crea un nuevo reporte (perdida o encontrada).")
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente")
    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> reportarMascota(@RequestBody Mascota mascota) {

        // Guardamos la mascota en la base de datos
        Mascota nuevaMascota = mascotaRepository.save(mascota);

        // Armamos el JSON de respuesta exacto que exige el frontend
        Map<String, Object> response = new HashMap<>();
        response.put("id", nuevaMascota.getId());
        response.put("status", "success");
        response.put("message", "Reporte creado exitosamente");

        // Retornamos 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Sugerencias de búsqueda", description = "Autocompletado en tiempo real por nombre.")
    @GetMapping("/suggestions")
    public ResponseEntity<List<Mascota>> obtenerSugerencias(@RequestParam("q") String q) {
        List<Mascota> sugerencias = mascotaRepository.findByNameContainingIgnoreCase(q);
        return ResponseEntity.ok(sugerencias);
    }

    @Operation(summary = "Obtener mascotas de un usuario", description = "Lista el historial de reportes de un usuario específico.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mascotaRepository.findByUsuarioId(usuarioId));
    }
    
    // Endpoint 3: Obtener mascota por ID (Opcional pero muy útil para el frontend)
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Long id) {
        return mascotaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}