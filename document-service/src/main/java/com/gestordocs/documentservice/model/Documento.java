package com.gestordocs.documentservice.model;

import com.gestordocs.documentservice.model.enums.EstadoDocumento;
import com.gestordocs.documentservice.model.enums.EstadoEntrega;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String oc; 

    @Column(name = "creado_por_usuario_id", nullable = false)
    private Integer creadoPorUsuarioId;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion = Instant.now();

    // Mapeo del ENUM
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "estado_documento", nullable = false)
    private EstadoDocumento estado = EstadoDocumento.PENDIENTE_COMPRAS;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_entrega", columnDefinition = "estado_entrega")
    private EstadoEntrega estadoEntrega;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchivoAsociado> anexos;

    // --- Constructor, Getters y Setters (Necesarios para JPA) ---
    public Documento() {}

    // Implementa aquí todos los Getters y Setters (ej: Alt+Insert en IntelliJ o Source->Generate en Eclipse)
    
    // Ejemplo:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOc() { return oc; }
    public void setOc(String oc) { this.oc = oc; }
    public Integer getCreadoPorUsuarioId() { return creadoPorUsuarioId; }
    public void setCreadoPorUsuarioId(Integer creadoPorUsuarioId) { this.creadoPorUsuarioId = creadoPorUsuarioId; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Instant fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public EstadoDocumento getEstado() { return estado; }
    public void setEstado(EstadoDocumento estado) { this.estado = estado; }
    public EstadoEntrega getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(EstadoEntrega estadoEntrega) { this.estadoEntrega = estadoEntrega; }
    public List<ArchivoAsociado> getAnexos() { return anexos; }
    public void setAnexos(List<ArchivoAsociado> anexos) { this.anexos = anexos; }
}