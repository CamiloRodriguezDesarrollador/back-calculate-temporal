package com.plprv.PlataformaProveedores.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DocumentosProveedor {

    private Integer prvId;
    private String prvNombre;
    private Integer proId;
    private Integer sprId;
    private Long cantidad;

    private Integer crtId;

    private String tdcTd;

    public DocumentosProveedor(Integer prvId, String prvNombre, Integer proId, Integer sprId, Long cantidad, Integer crtId, String tdcTd) {
        this.prvId = prvId;
        this.prvNombre = prvNombre;
        this.proId = proId;
        this.sprId = sprId;
        this.cantidad = cantidad;
        this.crtId = crtId;
        this.tdcTd = tdcTd;
    }

    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
    }

    public String getPrvNombre() {
        return prvNombre;
    }

    public void setPrvNombre(String prvNombre) {
        this.prvNombre = prvNombre;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getSprId() {
        return sprId;
    }

    public void setSprId(Integer sprId) {
        this.sprId = sprId;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCrtId() {
        return crtId;
    }

    public void setCrtId(Integer crtId) {
        this.crtId = crtId;
    }

    public String getTdcTd() {
        return tdcTd;
    }

    public void setTdcTd(String tdcTd) {
        this.tdcTd = tdcTd;
    }
}
