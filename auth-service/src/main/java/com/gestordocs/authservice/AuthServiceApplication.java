// src/main/java/com/gestordocs/authservice/AuthServiceApplication.java

package com.gestordocs.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// No necesitamos excluir la seguridad aquí porque ya lo hicimos en application.properties
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}