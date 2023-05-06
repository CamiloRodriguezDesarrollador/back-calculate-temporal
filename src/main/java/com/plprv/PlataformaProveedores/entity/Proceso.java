package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_PROCESO")
public class Proceso {

    @Id
    @Column(name = "PRO_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Integer proId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "PRO_NOMBRE", length = 50)
    private String proNombre;

    @Column(name = "PRO_RESPONSABLE", length = 50)
    private String proResponsable;

    @Column(name = "PRO_CELULAR", length = 30)
    private String proCelular;

    @Column(name = "PRO_CARGO", length = 30)
    private String proCargo;

    @Column(name = "PRO_CORREO", length = 30)
    private String proCorreo;

    @Column(name = "PRO_CARPETA", length = 100)
    private String proCarpeta;

    @Column(name = "PRO_ESTADO", length = 10)
    private String proEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public String getProCarpeta() {
        return proCarpeta;
    }

    public void setProCarpeta(String proCarpeta) {
        this.proCarpeta = proCarpeta;
    }

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProResponsable() {
        return proResponsable;
    }

    public void setProResponsable(String proResponsable) {
        this.proResponsable = proResponsable;
    }

    public String getProCelular() {
        return proCelular;
    }

    public void setProCelular(String proCelular) {
        this.proCelular = proCelular;
    }

    public String getProCargo() {
        return proCargo;
    }

    public void setProCargo(String proCargo) {
        this.proCargo = proCargo;
    }

    public String getProCorreo() {
        return proCorreo;
    }

    public void setProCorreo(String proCorreo) {
        this.proCorreo = proCorreo;
    }

    public String getProEstado() {
        return proEstado;
    }

    public void setProEstado(String proEstado) {
        this.proEstado = proEstado;
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
