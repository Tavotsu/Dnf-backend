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

        /*
        aqui se envia una notificacion de al usuario, al momento de hacer una coincidencia
        llega una alerta al usuario con el nombre de la mascota y el mensaje de alerta 

        @param alerta: Objeto que contiene la información de la alerta a enviar (correo del dueño, nombre de la mascota, mensaje de alerta)
        @param asunto: Asunto del correo a enviar, que incluye el nombre de la mascota para personalizar la notificación
        @return: Respuesta indicando si la notificación se envió con éxito o si hubo
        */
        
        try {
            emailService.enviarCorreo(alerta.getCorreoDueno(), asunto, alerta.getMensaje());
            return ResponseEntity.ok("Notificación enviada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }
}