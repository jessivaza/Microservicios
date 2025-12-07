package com.gestordocs.rbacservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestordocs.rbacservice.model.Permiso;
import com.gestordocs.rbacservice.model.Rol;
import com.gestordocs.rbacservice.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Optional<Rol> buscarPorId(Integer id) {
        return rolRepository.findById(id);
    }

    public List<String> obtenerCodigosPermisosPorRol(Integer idRol) {
        Optional<Rol> rolOpt = rolRepository.findById(idRol);
        if (rolOpt.isEmpty()) {
            return List.of();
        }

        Set<Permiso> permisos = rolOpt.get().getPermisos();

        return permisos.stream()
                .map(Permiso::getCodigo)
                .collect(Collectors.toList());
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }
}
