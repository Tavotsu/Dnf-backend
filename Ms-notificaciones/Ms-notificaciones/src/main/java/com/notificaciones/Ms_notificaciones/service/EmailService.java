package com.notificaciones.Ms_notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo);
        message.setFrom("no-reply@sanosysalvos.cl"); // Remitente ficticio o real

        mailSender.send(message);
    }
}