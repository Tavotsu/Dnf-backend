package com.dcknotfnd.Ms_mascota.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mascotas")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especie;

    private String raza;
    private String color;
    private String tamano;

    @Column(length = 500)
    private String caracteristicasFisicas;

    private String fotoUrl;

    // Ya no usamos @ManyToOne. Solo guardamos el ID del usuario como referencia
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;


}
