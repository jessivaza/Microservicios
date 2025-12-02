// src/main/java/com/gestordocs/documentservice/repository/RepositorioDocumento.java

package main.java.com.gestordocs.documentservice.repository;

import com.gestordocs.documentservice.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepositorioDocumento extends JpaRepository<Documento, Long> {
    
    Optional<Documento> findByOc(String oc);

}