package com.dcknotfnd.ms_coincidencias.dto;

import lombok.Data;

@Data
public class MascotaDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private String tamano;
    private String fotoUrl;
}