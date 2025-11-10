// src/main/java/com/gestordocs/userservice/UserServiceApplication.java

package com.gestordocs.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// Excluir la auto-configuración de seguridad para que nuestra clase WebSecurityConfig la controle
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}