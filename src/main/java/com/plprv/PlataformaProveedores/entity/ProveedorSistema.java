package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "PROVEEDOR")
public class ProveedorSistema {
    @Column(name = "TDC_TD", length = 30)
    private String tdcTd;

    @Id
    @Column(name = "NIT_ND")
    private Integer nitNd;

    @Column(name = "PRV_ESTADO", length = 30)
    private String prvEstado;

    @Column(name = "PRV_ACTIVIDAD")
    private Integer prvActivodad;

    @Column(name = "PDM_CONSEC")
    private Integer pdcConsec;


    public Integer getPrvActivodad() {
        return prvActivodad;
    }

    public void setPrvActivodad(Integer prvActivodad) {
        this.prvActivodad = prvActivodad;
    }

    public Integer getPdcConsec() {
        return pdcConsec;
    }

    public void setPdcConsec(Integer pdcConsec) {
        this.pdcConsec = pdcConsec;
    }

    public String getTdcTd() {
        return tdcTd;
    }

    public void setTdcTd(String tdcTd) {
        this.tdcTd = tdcTd;
    }

    public Integer getNitNd() {
        return nitNd;
    }

    public void setNitNd(Integer nitNd) {
        this.nitNd = nitNd;
    }

    public String getPrvEstado() {
        return prvEstado;
    }

    public void setPrvEstado(String prvEstado) {
        this.prvEstado = prvEstado;
    }

    private static final long serialVersionUID = 1L;



}
