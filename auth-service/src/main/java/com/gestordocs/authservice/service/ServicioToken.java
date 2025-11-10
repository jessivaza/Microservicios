// src/main/java/com/gestordocs/authservice/service/ServicioToken.java

package com.gestordocs.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServicioToken {

    @Value("${jwt.secret.key}")
    private String claveSecreta;

    @Value("${jwt.expiration.ms}")
    private long tiempoExpiracionMs;

    private Key claveFirma;

    private Key getClaveFirma() {
        if (this.claveFirma == null) {
            // Se genera una clave segura a partir del secreto usando HMAC SHA-256
            this.claveFirma = Keys.hmacShaKeyFor(claveSecreta.getBytes());
        }
        return this.claveFirma;
    }

    /**
     * Genera un token JWT para un usuario.
     */
    public String generarToken(Long usuarioId, Integer rolId) {
        Map<String, Object> claims = new HashMap<>();
        // Agregar información de identidad (claims) al cuerpo del token
        claims.put("rol", rolId);
        claims.put("id_usuario", usuarioId);

        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + tiempoExpiracionMs);

        return Jwts.builder()
                .setClaims(claims) 
                .setSubject(usuarioId.toString()) 
                .setIssuedAt(ahora) 
                .setExpiration(fechaExpiracion) 
                .signWith(getClaveFirma(), SignatureAlgorithm.HS256) 
                .compact();
    }
}