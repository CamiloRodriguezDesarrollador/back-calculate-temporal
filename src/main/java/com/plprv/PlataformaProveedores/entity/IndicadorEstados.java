package com.plprv.PlataformaProveedores.entity;

public class IndicadorEstados {
    private String preEstado;
    private Long cantidad;

    public IndicadorEstados(String preEstado, Long cantidad) {
        this.preEstado = preEstado;
        this.cantidad = cantidad;
    }

    public String getPreEstado() {
        return preEstado;
    }

    public void setPreEstado(String preEstado) {
        this.preEstado = preEstado;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
