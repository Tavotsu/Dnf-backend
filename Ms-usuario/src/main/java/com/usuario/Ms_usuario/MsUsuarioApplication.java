package com.usuario.Ms_usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsUsuarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsuarioApplication.class, args);
    }

}