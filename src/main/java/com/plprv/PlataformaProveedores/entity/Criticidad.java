package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_CRITICIDAD")
public class Criticidad {

    @Id
    @Column(name = "CRT_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer crtId;

    @Column(name = "ID_EMPPAL", length = 30)
    private int idEmppal;

    @Column(name = "CRT_NOMBRE", length = 50)
    private String crtNombre;

    @Column(name = "CRT_DETALLE", length = 100)
    private String crtDetalle;

    @Column(name = "CRT_ESTADO", length = 10)
    private String crtEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;
    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getCrtId() {
        return crtId;
    }

    public void setCrtId(Integer crtId) {
        this.crtId = crtId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getCrtNombre() {
        return crtNombre;
    }

    public void setCrtNombre(String crtNombre) {
        this.crtNombre = crtNombre;
    }

    public String getCrtDetalle() {
        return crtDetalle;
    }

    public void setCrtDetalle(String crtDetalle) {
        this.crtDetalle = crtDetalle;
    }

    public String getCrtEstado() {
        return crtEstado;
    }

    public void setCrtEstado(String crtEstado) {
        this.crtEstado = crtEstado;
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
