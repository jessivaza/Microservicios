package com.gestordocs.rbacservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestordocs.rbacservice.model.Permiso;
import com.gestordocs.rbacservice.service.PermisoService;

@RestController
@RequestMapping("/api/permisos")
@CrossOrigin(origins = "http://localhost:3000")
public class PermisoController {

    private final PermisoService permisoService;

    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    @GetMapping
    public List<Permiso> listarTodos() {
        return permisoService.listarTodos();
    }

    // Si quieres poder crear permisos vía API:
    @PostMapping
    public Permiso crear(@RequestBody Permiso permiso) {
        return permisoService.guardar(permiso);
    }
}
