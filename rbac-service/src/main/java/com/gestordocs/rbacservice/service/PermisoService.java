package com.gestordocs.rbacservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestordocs.rbacservice.model.Permiso;
import com.gestordocs.rbacservice.repository.PermisoRepository;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    public List<Permiso> listarTodos() {
        return permisoRepository.findAll();
    }

    public Permiso guardar(Permiso permiso) {
        return permisoRepository.save(permiso);
    }
}
