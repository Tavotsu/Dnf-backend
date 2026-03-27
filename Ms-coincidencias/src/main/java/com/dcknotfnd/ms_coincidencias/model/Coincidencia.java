package com.dcknotfnd.ms_coincidencias.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coincidencias")
@Data
public class Coincidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String especieBuscada;
    private String colorBuscado;
    private String estado; // Ej: "PENDIENTE", "CONFIRMADO", "RECHAZADO"
}