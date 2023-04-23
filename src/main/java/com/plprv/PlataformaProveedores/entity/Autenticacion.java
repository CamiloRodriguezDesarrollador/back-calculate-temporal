package com.plprv.PlataformaProveedores.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_AUTENTICATIONPRV")
public class Autenticacion {
    @Id
    @Column(name = "AUT_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer autId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "AUT_CORREO", length = 150)
    private String autCorreo;

    @Column(name = "AUT_CONTRASENA", length = 150)
    private String autContrasena;

    @Column(name = "TDC_TD", length = 50)
    private String tdcTd;

    @Column(name = "PRV_ND", length = 100)
    private String prvNd;

    @Column(name = "PRV_NOMBRE", length = 100)
    private String prvNombre;

    @Column(name = "PRO_ID", length = 100)
    private Integer proId;

    @Column(name = "SPR_ID", length = 100)
    private Integer sprId;
    @Column(name = "PRV_CELULAR", length = 100)
    private String prvCelular;

    @Column(name = "PAI_NOMBRE", length = 100)
    private String paiNombre;

    @Column(name = "DPT_NOMBRE", length = 100)
    private String dptNombre;

    @Column(name = "CIU_NOMBRE", length = 100)
    private String ciuNombre;

    @Column(name = "PRV_DIRECCION", length = 150)
    private String prvDireccion;

    @Column(name = "AUT_CODIGOCORREO", length = 30)
    private String autCodigoCorreo;

    @Column(name = "AUT_ESTADO", length = 10)
    private String autEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;
    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getAutId() {
        return autId;
    }

    public void setAutId(Integer autId) {
        this.autId = autId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getAutCorreo() {
        return autCorreo;
    }

    public void setAutCorreo(String autCorreo) {
        this.autCorreo = autCorreo;
    }

    public String getAutContrasena() {
        return autContrasena;
    }

    public void setAutContrasena(String autContrasena) {
        this.autContrasena = autContrasena;
    }

    public String getTdcTd() {
        return tdcTd;
    }

    public void setTdcTd(String tdcTd) {
        this.tdcTd = tdcTd;
    }

    public void setPrvNd(String prvNd) {
        this.prvNd = prvNd;
    }

    public void setPrvNombre(String prvNombre) {
        this.prvNombre = prvNombre;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public void setSprId(Integer sprId) {
        this.sprId = sprId;
    }


    public String getPrvDireccion() {
        return prvDireccion;
    }

    public void setPrvDireccion(String prvDireccion) {
        this.prvDireccion = prvDireccion;
    }

    public void setPrvCelular(String prvCelular) {
        this.prvCelular = prvCelular;
    }

    public void setPaiNombre(String paiNombre) {
        this.paiNombre = paiNombre;
    }

    public void setDptNombre(String dptNombre) {
        this.dptNombre = dptNombre;
    }

    public void setCiuNombre(String ciuNombre) {
        this.ciuNombre = ciuNombre;
    }

    public String getPrvNd() {
        return prvNd;
    }

    public String getPrvNombre() {
        return prvNombre;
    }

    public Integer getProId() {
        return proId;
    }

    public Integer getSprId() {
        return sprId;
    }

    public String getPrvCelular() {
        return prvCelular;
    }

    public String getPaiNombre() {
        return paiNombre;
    }

    public String getDptNombre() {
        return dptNombre;
    }

    public String getCiuNombre() {
        return ciuNombre;
    }

    public String getAutCodigoCorreo() {
        return autCodigoCorreo;
    }

    public void setAutCodigoCorreo(String autCodigoCorreo) {
        this.autCodigoCorreo = autCodigoCorreo;
    }

    public String getAutEstado() {
        return autEstado;
    }

    public void setAutEstado(String autEstado) {
        this.autEstado = autEstado;
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
