package com.gestordocs.documentservice.model.enums;

public enum EstadoEntrega {
    EN_ESPERA("En Espera"),
    EN_RUTA("En Ruta"),
    ENTREGADO("Entregado");

    private final String valor;

    EstadoEntrega(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}