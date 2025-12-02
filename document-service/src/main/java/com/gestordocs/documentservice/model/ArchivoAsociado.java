package main.java.com.gestordocs.documentservice.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "archivos_asociados")
public class ArchivoAsociado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "archivo_storage_id", nullable = false)
    private UUID archivoStorageId; // Almacena el UUID del Storage Service

    @Column(name = "tipo_archivo", length = 50, nullable = false)
    private String tipoArchivo; 

    @Column(nullable = false, length = 50)
    private String area;

    @Column(nullable = false)
    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    // --- Constructor, Getters y Setters (Necesarios para JPA) ---
    public ArchivoAsociado() {}
    
    // Implementa aquí todos los Getters y Setters
    
    // Ejemplo:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UUID getArchivoStorageId() { return archivoStorageId; }
    public void setArchivoStorageId(UUID archivoStorageId) { this.archivoStorageId = archivoStorageId; }
    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }
}