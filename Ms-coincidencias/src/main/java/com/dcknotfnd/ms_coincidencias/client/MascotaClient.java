package com.dcknotfnd.ms_coincidencias.client;

import com.dcknotfnd.ms_coincidencias.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "ms-mascota", url = "http://localhost:8080")
public interface MascotaClient {


    @GetMapping("/api/mascotas")
    List<MascotaDTO> obtenerTodasLasMascotas();
}