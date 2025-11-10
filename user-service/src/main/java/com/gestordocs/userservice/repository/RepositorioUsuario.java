// src/main/java/com/gestordocs/userservice/repository/RepositorioUsuario.java

package com.gestordocs.userservice.repository;

import com.gestordocs.userservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);

}