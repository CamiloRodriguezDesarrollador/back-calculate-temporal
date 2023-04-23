package com.plprv.PlataformaProveedores.entity;

public class IndicadorProcesos {
    private String prNombre;
    private Long cantidad;

    public IndicadorProcesos(String prNombre, Long cantidad) {
        this.prNombre = prNombre;
        this.cantidad = cantidad;
    }

    public String getproNombre() {
        return prNombre;
    }

    public void setproNombre(String proNombre) {
        this.prNombre = proNombre;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
