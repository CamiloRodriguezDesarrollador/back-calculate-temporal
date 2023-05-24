package com.plprv.PlataformaProveedores.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_SUBPROCESO")
public class SubProceso {

    @Id
    @Column(name = "SPR_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    
    private Integer sprId;

    @Column(name = "ID_EMPPAL", length = 30)
    private int idEmppal;

    @Column(name = "SPR_NOMBRE", length = 50)
    private String sprNombre;

    @Column(name = "PRO_ID")
    private int proId;

    @Column(name = "SPR_ESTADO", length = 10)
    private String sprEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getSprId() {
        return sprId;
    }

    public void setSprId(Integer sprId) {
        this.sprId = sprId;
    }

    public int getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(int idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getSprNombre() {
        return sprNombre;
    }

    public void setSprNombre(String sprNombre) {
        this.sprNombre = sprNombre;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getSprEstado() {
        return sprEstado;
    }

    public void setSprEstado(String sprEstado) {
        this.sprEstado = sprEstado;
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

