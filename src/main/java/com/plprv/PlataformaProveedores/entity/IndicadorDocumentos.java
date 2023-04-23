package com.plprv.PlataformaProveedores.entity;

public class IndicadorDocumentos {
    private String prdDocumento;
    private Long cantidad;

    public IndicadorDocumentos(String prdDocumento, Long cantidad) {
        this.prdDocumento = prdDocumento;
        this.cantidad = cantidad;
    }

    public String getprdDocumento() {
        return prdDocumento;
    }

    public void setprdDocumento(String prdDocumento) {
        this.prdDocumento = prdDocumento;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
