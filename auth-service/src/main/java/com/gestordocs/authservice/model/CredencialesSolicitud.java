// src/main/java/com/gestordocs/authservice/model/CredencialesSolicitud.java

package com.gestordocs.authservice.model;

/**
 * Representa los datos que el cliente envía en el POST de login.
 */
public class CredencialesSolicitud {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}