package com.comunidad.Ms_comunidad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "historias")
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La historia debe tener un título")
    private String title;

    @NotBlank(message = "El nombre de la familia es obligatorio")
    private String family;

    private String location;
    
    @NotBlank(message = "El contenido de la historia no puede estar vacío")
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 1000)
    private String image;
    
    private String timeAgo;

    public Historia() {
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }
}