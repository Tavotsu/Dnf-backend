package com.usuario.Ms_usuario.model;


import jakarta.persistence.*;
import lombok.Data;
@Data
class Coincidencia {}

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    //ej ciudadano, clinica, refugio , municipalidad
    @Column(nullable = false)
    private String rol;


}
