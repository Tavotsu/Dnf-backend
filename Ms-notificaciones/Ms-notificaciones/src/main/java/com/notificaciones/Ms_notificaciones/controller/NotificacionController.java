package com.notificaciones.Ms_notificaciones.controller;

import com.notificaciones.Ms_notificaciones.dto.AlertaMatchDTO;
import com.notificaciones.Ms_notificaciones.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar-alerta")
    public ResponseEntity<String> enviarNotificacion(@RequestBody AlertaMatchDTO alerta) {
        String asunto = "¡Buenas noticias! Posible coincidencia para " + alerta.getNombreMascota();
        
        try {
            emailService.enviarCorreo(alerta.getCorreoDueno(), asunto, alerta.getMensaje());
            return ResponseEntity.ok("Notificación enviada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }
}