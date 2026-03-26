package com.dcknotfnd.Ms_mascota.model;
import jakarta.persistence.*;

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

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getCaracteristicasFisicas() { return caracteristicasFisicas; }
    public void setCaracteristicasFisicas(String caracteristicasFisicas) { this.caracteristicasFisicas = caracteristicasFisicas; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
