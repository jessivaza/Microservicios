package com.gestordocs.rbacservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestordocs.rbacservice.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

    Optional<Permiso> findByCodigo(String codigo);
}
