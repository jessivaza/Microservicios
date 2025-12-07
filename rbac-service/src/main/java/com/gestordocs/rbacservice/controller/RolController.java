package com.gestordocs.rbacservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestordocs.rbacservice.model.Rol;
import com.gestordocs.rbacservice.service.RolService;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listarRoles() {
        return rolService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRol(@PathVariable Integer id) {
        return rolService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint que usará auth-service:
     * GET /api/roles/{id}/permisos
     * Devuelve solo la lista de códigos de permiso.
     */
    @GetMapping("/{id}/permisos")
    public ResponseEntity<List<String>> obtenerPermisosPorRol(@PathVariable Integer id) {
        var codigos = rolService.obtenerCodigosPermisosPorRol(id);
        if (codigos.isEmpty()) {
            // puede ser rol sin permisos o rol inexistente; aquí asumimos 404 si no existe
            return rolService.buscarPorId(id).isEmpty()
                    ? ResponseEntity.notFound().build()
                    : ResponseEntity.ok(codigos);
        }
        return ResponseEntity.ok(codigos);
    }

    // Opcional: crear rol
    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.guardar(rol);
    }
}
