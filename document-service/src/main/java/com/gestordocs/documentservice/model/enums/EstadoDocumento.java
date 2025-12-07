package com.gestordocs.documentservice.model.enums;

public enum EstadoDocumento {
    PENDIENTE_COMPRAS("Pendiente Compras"),
    PENDIENTE_FACTURACION("Pendiente Facturación"),
    PENDIENTE_OPERACIONES("Pendiente Operaciones"),
    COMPLETADO("Completado");
    
    private final String valor;

    EstadoDocumento(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}