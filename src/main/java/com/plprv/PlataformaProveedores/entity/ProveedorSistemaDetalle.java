package com.plprv.PlataformaProveedores.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PROVEEDOR_CLASIFICA")
public class ProveedorSistemaDetalle {

    @Id
    @Column(name = "SECUENCIA")
    private Integer secuencia;

    @Column(name = "DESCRIPCION", length = 100)
    private String prvDetalle;

    @Column(name = "PDM_CONSEC")
    private Integer pdmConsecutivo;

    @Column(name = "PDM_PROCESO", length = 100)
    private String pdmProceso;

    public String getPrvDetalle() {
        return prvDetalle;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public void setPrvDetalle(String prvDetalle) {
        this.prvDetalle = prvDetalle;
    }

    public Integer getPdmConsecutivo() {
        return pdmConsecutivo;
    }

    public void setPdmConsecutivo(Integer pdmConsecutivo) {
        this.pdmConsecutivo = pdmConsecutivo;
    }

    public String getPdmProceso() {
        return pdmProceso;
    }

    public void setPdmProceso(String pdmProceso) {
        this.pdmProceso = pdmProceso;
    }

    private static final long serialVersionUID = 1L;



}
