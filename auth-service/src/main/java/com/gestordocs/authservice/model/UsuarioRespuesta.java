// src/main/java/com/gestordocs/authservice/model/UsuarioRespuesta.java

package com.gestordocs.authservice.model;

/**
 * Mapea la información clave recibida del User Service.
 */
public class UsuarioRespuesta {
    private Long id;
    private String email;
    private String passwordHash; // Clave para la verificación
    private Integer rolId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }
}