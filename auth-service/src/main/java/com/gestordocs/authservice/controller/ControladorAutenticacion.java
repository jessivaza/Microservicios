package com.gestordocs.authservice.controller;

import com.gestordocs.authservice.model.CredencialesSolicitud;
import com.gestordocs.authservice.model.UsuarioRespuesta;
import com.gestordocs.authservice.service.ServicioToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ControladorAutenticacion {

    private final ServicioToken servicioToken;
    private final BCryptPasswordEncoder codificadorContrasena;
    private final WebClient webClientUsuario; 

    // Inyección de dependencias
    public ControladorAutenticacion(
            ServicioToken servicioToken, 
            @Value("${app.services.user.url}") String urlServicioUsuario) {
        
        this.servicioToken = servicioToken;
        this.codificadorContrasena = new BCryptPasswordEncoder();
        this.webClientUsuario = WebClient.builder().baseUrl(urlServicioUsuario).build();
    }

    /**
     * Endpoint: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody CredencialesSolicitud credenciales) {
        
        UsuarioRespuesta usuarioDb;
        
        try {
            // 1. Obtener usuario del User Service (Llamada HTTP a http://localhost:3001/api/usuarios/email/{email})
            usuarioDb = webClientUsuario.get()
                    .uri("/{email}", credenciales.getEmail()) 
                    .retrieve()
                    .bodyToMono(UsuarioRespuesta.class)
                    .block(); 
            
            if (usuarioDb == null || usuarioDb.getPasswordHash() == null) {
                // Esto ocurriría si la respuesta es 200 OK pero el cuerpo es incompleto o nulo (escenario muy raro)
                return new ResponseEntity<>(Map.of("error", "Credenciales inválidas: datos de usuario incompletos."), HttpStatus.UNAUTHORIZED);
            }
            
        } catch (WebClientResponseException.NotFound e) {
            // 2. Manejo de error 404: El User Service dice que el usuario NO existe
            return new ResponseEntity<>(Map.of("error", "Credenciales inválidas: usuario no encontrado."), HttpStatus.UNAUTHORIZED);
            
        } catch (WebClientResponseException e) {
            // 3. Manejo de otros errores HTTP (ej. 5xx del User Service, que es un error interno)
            System.err.println("Error HTTP al contactar User Service: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return new ResponseEntity<>(Map.of("error", "Error interno al contactar el servicio de usuarios."), HttpStatus.INTERNAL_SERVER_ERROR);
            
        } catch (Exception e) {
             // 4. Manejo de errores de conexión/red (El servicio 3001 está inalcanzable)
             System.err.println("Error de conexión/parseo: " + e.getMessage());
             return new ResponseEntity<>(Map.of("error", "Error de conexión: El servicio de usuarios (3001) es inalcanzable."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 5. Verificar la contraseña
        // Si llegamos aquí, la conexión funcionó. Ahora se comprueba la clave.
        if (!codificadorContrasena.matches(credenciales.getPassword(), usuarioDb.getPasswordHash())) {
            // Contraseña incorrecta
            return new ResponseEntity<>(Map.of("error", "Credenciales inválidas: contraseña incorrecta."), HttpStatus.UNAUTHORIZED); 
        }

        // 6. Generar el Token JWT
        String token = servicioToken.generarToken(usuarioDb.getId(), usuarioDb.getRolId());

        // 7. Devolver el token
        return ResponseEntity.ok(Map.of("token", token));
    }
}