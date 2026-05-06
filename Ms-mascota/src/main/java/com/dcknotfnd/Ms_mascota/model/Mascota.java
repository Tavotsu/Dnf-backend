package com.dcknotfnd.Ms_mascota.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "Debes especificar si es Perro, Gato, etc.")
    private String type; 

    private String breed; 
    private String gender; 

    @NotBlank(message = "El estado (perdido/encontrado) es obligatorio")
    private String status; 

    @NotBlank(message = "La ubicación es obligatoria")
    private String location; 
    
    @NotNull(message = "La latitud es requerida para el mapa")
    private Double latitude; 
    
    @NotNull(message = "La longitud es requerida para el mapa")
    private Double longitude; 

    private String timeAgo;
    
    @Column(length = 1000)
    private String image;

    public Mascota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}