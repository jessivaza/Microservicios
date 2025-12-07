package com.gestordocs.rbacservice.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único que usará el front (tiene que coincidir con tu enum Permiso en el front)
    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(length = 255)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    private Set<Rol> roles = new HashSet<>();

    public Permiso() {
    }

    public Permiso(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Rol> getRoles() {
        return roles;
    }
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
