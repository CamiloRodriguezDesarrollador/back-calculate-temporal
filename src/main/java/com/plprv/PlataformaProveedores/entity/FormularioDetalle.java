package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_FORMULARIODETALLE")
public class FormularioDetalle {

    @Id
    @Column(name = "FOD_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer fodId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "FOD_NOMBRE", length = 50)
    private String fodNombre;

    @Column(name = "FOR_ID")
    private int forId;

    @Column(name = "FOD_TIPO", length = 30)
    private String fodTipo;

    @Column(name = "FOD_ADJUNTO", length = 100)
    private String fodAdjunto;

    @Column(name = "FOD_VIGENCIA", length = 30)
    private String fodVigencia;

    @Column(name = "FOD_ESTADO", length = 30)
    private String fodEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getFodId() {
        return fodId;
    }

    public void setFodId(Integer fodId) {
        this.fodId = fodId;
    }

    public int getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getFodNombre() {
        return fodNombre;
    }

    public void setFodNombre(String fodNombre) {
        this.fodNombre = fodNombre;
    }

    public int getForId() {
        return forId;
    }

    public void setForId(int forId) {
        this.forId = forId;
    }

    public String getFodTipo() {
        return fodTipo;
    }

    public void setFodTipo(String fodTipo) {
        this.fodTipo = fodTipo;
    }

    public String getFodAdjunto() {
        return fodAdjunto;
    }

    public void setFodAdjunto(String fodAdjunto) {
        this.fodAdjunto = fodAdjunto;
    }

    public String getFodVigencia() {
        return fodVigencia;
    }

    public void setFodVigencia(String fodVigencia) {
        this.fodVigencia = fodVigencia;
    }

    public String getFodEstado() {
        return fodEstado;
    }

    public void setFodEstado(String fodEstado) {
        this.fodEstado = fodEstado;
    }

    public Date getAudFecha() {
        return audFecha;
    }

    public void setAudFecha(Date audFecha) {
        this.audFecha = audFecha;
    }

    public String getAudUsuario() {
        return audUsuario;
    }

    public void setAudUsuario(String audUsuario) {
        this.audUsuario = audUsuario;
    }

    private static final long serialVersionUID = 1L;

}
