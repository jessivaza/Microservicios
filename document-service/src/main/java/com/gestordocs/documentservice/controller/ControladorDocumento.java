// src/main/java/com/gestordocs/documentservice/controller/ControladorDocumento.java

package com.gestordocs.documentservice.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestordocs.documentservice.model.ArchivoAsociado;
import com.gestordocs.documentservice.model.Documento;
import com.gestordocs.documentservice.model.enums.EstadoDocumento;
import com.gestordocs.documentservice.repository.RepositorioDocumento;

@RestController
@RequestMapping("/api/documentos")
public class ControladorDocumento {

    private final RepositorioDocumento repositorioDocumento;
    // Aquí se inyectarían servicios para el Storage (3005) y Audit (3006)

    public ControladorDocumento(RepositorioDocumento repositorioDocumento) {
        this.repositorioDocumento = repositorioDocumento;
    }

    /**
     * POST /api/documentos
     * Crea un nuevo documento, iniciando el flujo de trabajo (VENTAS).
     * @param solicitud La solicitud debe incluir OC, creadoPorUsuarioId y datos del archivo principal.
     */
    @PostMapping
    public ResponseEntity<?> crearNuevoDocumento(@RequestBody Documento solicitud) {
        
        // Asumiendo que la solicitud viene con al menos 1 anexo (el archivo principal)
        if (solicitud.getOc() == null || solicitud.getCreadoPorUsuarioId() == null || solicitud.getAnexos() == null || solicitud.getAnexos().isEmpty()) {
            return new ResponseEntity<>("Faltan campos obligatorios.", HttpStatus.BAD_REQUEST);
        }

        if (repositorioDocumento.findByOc(solicitud.getOc()).isPresent()) {
            return new ResponseEntity<>("La OC ya está registrada.", HttpStatus.CONFLICT);
        }

        // 1. Crear el objeto Documento
        Documento nuevoDocumento = new Documento();
        nuevoDocumento.setOc(solicitud.getOc());
        nuevoDocumento.setCreadoPorUsuarioId(solicitud.getCreadoPorUsuarioId());
        nuevoDocumento.setFechaCreacion(Instant.now());
        nuevoDocumento.setEstado(EstadoDocumento.PENDIENTE_COMPRAS); 

        // 2. Asociar el archivo principal y enlazarlo
        ArchivoAsociado anexoPrincipal = solicitud.getAnexos().get(0);
        anexoPrincipal.setDocumento(nuevoDocumento); 
        
        // 3. Guardar el Documento (JPA guarda el anexo en cascada)
        Documento documentoGuardado = repositorioDocumento.save(nuevoDocumento);

        // [NOTA]: Aquí se llamarían asíncronamente al Audit Service y, posiblemente, al Storage Service.

        // Retornar el documento creado
        return new ResponseEntity<>(documentoGuardado, HttpStatus.CREATED);
    }
    
    /**
     * GET /api/documentos/{id}
     * Obtiene un documento por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtenerDocumentoPorId(@PathVariable Long id) {
        return repositorioDocumento.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/documentos/estado/{id}
     * Actualiza el estado del flujo de trabajo (solo un ejemplo).
     * @param nuevoEstadoString El nuevo estado como string (ej: "PENDIENTE_FACTURACION").
     */
@PutMapping("/estado/{id}")
public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody String nuevoEstadoString) {
    return repositorioDocumento.findById(id)
        .map(documento -> {
            try {
                EstadoDocumento nuevoEstado = EstadoDocumento.valueOf(nuevoEstadoString.toUpperCase());
                documento.setEstado(nuevoEstado);
                Documento actualizado = repositorioDocumento.save(documento);

                // 200 OK con el documento actualizado
                return ResponseEntity.ok(actualizado);
            } catch (IllegalArgumentException e) {
                // 400 Si el estado no existe
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estado no válido: " + nuevoEstadoString);
            }
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado")); // 404
}
}