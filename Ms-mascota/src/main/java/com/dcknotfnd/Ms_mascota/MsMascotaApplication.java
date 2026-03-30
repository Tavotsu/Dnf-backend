package com.dcknotfnd.Ms_mascota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsMascotaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMascotaApplication.class, args);
	}

}
