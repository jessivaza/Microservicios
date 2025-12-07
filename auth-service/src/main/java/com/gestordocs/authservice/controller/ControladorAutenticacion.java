package com.gestordocs.authservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gestordocs.authservice.model.CredencialesSolicitud;
import com.gestordocs.authservice.model.UsuarioRespuesta;
import com.gestordocs.authservice.service.ServicioToken;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorAutenticacion {

    private final ServicioToken servicioToken;
    private final BCryptPasswordEncoder codificadorContrasena = new BCryptPasswordEncoder();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.services.user.url}")
    private String userServiceBaseUrl;

    @Value("${app.services.rbac.url}")
    private String rbacServiceBaseUrl;

    public ControladorAutenticacion(ServicioToken servicioToken) {
        this.servicioToken = servicioToken;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody CredencialesSolicitud credenciales) {

        try {
            // 1️⃣ Llamar al User Service para obtener usuario por email
            String url = userServiceBaseUrl + "/email/{email}";

            ResponseEntity<UsuarioRespuesta> respuesta =
                    restTemplate.getForEntity(url, UsuarioRespuesta.class, credenciales.getEmail());

            if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas: usuario no encontrado."));
            }

            UsuarioRespuesta usuarioDb = respuesta.getBody();

            if (usuarioDb.getPasswordHash() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas: datos de usuario incompletos."));
            }

            // 2️⃣ Verificar la contraseña con BCrypt
            boolean contrasenaValida =
                    codificadorContrasena.matches(credenciales.getPassword(), usuarioDb.getPasswordHash());

            if (!contrasenaValida) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas: contraseña incorrecta."));
            }

            // 3️⃣ Consultar permisos en el RBAC Service según el rol del usuario
            List<String> permisos = new ArrayList<>();
            try {
                String permisosUrl = rbacServiceBaseUrl + "/roles/{rolId}/permisos";

                ResponseEntity<List> respuestaPermisos =
                        restTemplate.getForEntity(permisosUrl, List.class, usuarioDb.getRolId());

                if (respuestaPermisos.getStatusCode().is2xxSuccessful()
                        && respuestaPermisos.getBody() != null) {
                    // cast seguro en tiempo de ejecución (la API devuelve List<String>)
                    permisos = (List<String>) (List<?>) respuestaPermisos.getBody();
                }
            } catch (Exception exPermisos) {
                // Si el RBAC falla, seguimos con permisos vacíos pero el login funciona
                exPermisos.printStackTrace();
            }

            // 4️⃣ Generar el Token JWT
            String token = servicioToken.generarToken(usuarioDb.getId(), usuarioDb.getRolId());

            // 5️⃣ Devolver token + datos de usuario + permisos
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userID", usuarioDb.getId(),
                    "nombres", usuarioDb.getNombres(),
                    "apellidos", usuarioDb.getApellidos(),
                    "email", usuarioDb.getEmail(),
                    "rolId", usuarioDb.getRolId(),
                    "permisos", permisos
            ));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas: usuario no encontrado."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno en auth-service."));
        }
    }
}
