package com.gestordocs.rbacservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestordocs.rbacservice.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
}
