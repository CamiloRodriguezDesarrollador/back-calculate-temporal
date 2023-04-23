package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_FORMULARIO")
public class Formulario {

    @Id
    @Column(name = "FOR_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int forId;
    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "FOR_NOMBRE", length = 50)
    private String forNombre;

    @Column(name = "FOR_DETALLE", length = 200)
    private String forDetalle;

    @Column(name = "FOR_TIPO", length = 30)
    private String forTipo;
    @Column(name = "FOR_ESTADO", length = 10)
    private String forEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public int getForId() {
        return forId;
    }

    public void setForId(int forId) {
        this.forId = forId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(int idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getForNombre() {
        return forNombre;
    }

    public void setForNombre(String forNombre) {
        this.forNombre = forNombre;
    }

    public String getForDetalle() {
        return forDetalle;
    }

    public void setForDetalle(String forDetalle) {
        this.forDetalle = forDetalle;
    }

    public String getForTipo() {
        return forTipo;
    }

    public void setForTipo(String forTipo) {
        this.forTipo = forTipo;
    }

    public String getForEstado() {
        return forEstado;
    }

    public void setForEstado(String forEstado) {
        this.forEstado = forEstado;
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
