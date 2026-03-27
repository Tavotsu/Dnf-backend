package com.dcknotfnd.ms_coincidencias.controller;

import com.dcknotfnd.ms_coincidencias.client.MascotaClient;
import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import com.dcknotfnd.ms_coincidencias.model.Coincidencia;
import com.dcknotfnd.ms_coincidencias.repository.CoincidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coincidencias")
public class CoincidenciaController {

    @Autowired
    private MascotaClient mascotaClient;

    @Autowired
    private CoincidenciaRepository coincidenciaRepository;

    @GetMapping("/buscar")
    public List<MascotaDTO> buscarCoincidencias(@RequestParam String especie, @RequestParam String color) {
        List<MascotaDTO> todasLasMascotas = mascotaClient.obtenerTodasLasMascotas();
        return todasLasMascotas.stream()
                .filter(m -> m.getEspecie().equalsIgnoreCase(especie) &&
                        m.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Coincidencia crearCoincidencia(@RequestBody Coincidencia coincidencia) {
        coincidencia.setEstado("PENDIENTE"); // Por defecto entra en pendiente
        return coincidenciaRepository.save(coincidencia);
    }


    @GetMapping
    public List<Coincidencia> listarCoincidencias() {
        return coincidenciaRepository.findAll();
    }


    @PutMapping("/{id}")
    public Coincidencia actualizarCoincidencia(@PathVariable Long id, @RequestBody Coincidencia coincidenciaActualizada) {
        return coincidenciaRepository.findById(id).map(coincidencia -> {
            coincidencia.setEstado(coincidenciaActualizada.getEstado());
            return coincidenciaRepository.save(coincidencia);
        }).orElseThrow(() -> new RuntimeException("Coincidencia no encontrada"));
    }


    @DeleteMapping("/{id}")
    public void eliminarCoincidencia(@PathVariable Long id) {
        coincidenciaRepository.deleteById(id);
    }
}