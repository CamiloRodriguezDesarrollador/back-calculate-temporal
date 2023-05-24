package com.plprv.PlataformaProveedores.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_PROVEEDOR")
public class Proveedor {

    @Id
    @Column(name = "PRV_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer prvId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "PRV_ND", length = 30)
    private String prvNd;

    @Column(name = "TDC_TD", length = 30)
    private String tdcTd;

    @Column(name = "PRV_NOMBRE", length = 150)
    private String prvNombre;

    @Column(name = "PRO_ID")
    private int proId;

    @Column(name = "SPR_ID")
    private int sprId;

    @Column(name = "PRV_CELULAR", length = 30)
    private String prvCelular;

    @Column(name = "PRV_DIRECCION", length = 150)
    private String prvDireccion;

    @Column(name = "PAI_NOMBRE", length = 50)
    private String paiNombre;

    @Column(name = "DPT_NOMBRE", length = 50)
    private String dptNombre;

    @Column(name = "CIU_NOMBRE", length = 50)
    private String ciuNombre;

    @Column(name = "PRV_CORREO", length = 150)
    private String prvCorreo;

    @Column(name = "PRV_TOKEN", length = 255)
    private String prvToken;

    @Column(name = "PRV_CARPETA", length = 100)
    private String prvCarpeta;

    @Column(name = "CRT_ID")
    private int crtId;

    @Column(name = "PRV_ESTADO", length = 10)
    private String prvEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getPrvNd() {
        return prvNd;
    }

    public void setPrvNd(String prvNd) {
        this.prvNd = prvNd;
    }

    public String getTdcTd() {
        return tdcTd;
    }

    public void setTdcTd(String tdcTd) {
        this.tdcTd = tdcTd;
    }

    public String getPrvNombre() {
        return prvNombre;
    }

    public void setPrvNombre(String prvNombre) {
        this.prvNombre = prvNombre;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getSprId() {
        return sprId;
    }

    public void setSprId(int sprId) {
        this.sprId = sprId;
    }

    public String getPrvCelular() {
        return prvCelular;
    }

    public void setPrvCelular(String prvCelular) {
        this.prvCelular = prvCelular;
    }

    public String getPrvDireccion() {
        return prvDireccion;
    }

    public void setPrvDireccion(String prvDireccion) {
        this.prvDireccion = prvDireccion;
    }

    public String getPaiNombre() {
        return paiNombre;
    }

    public void setPaiNombre(String paiNombre) {
        this.paiNombre = paiNombre;
    }

    public String getDptNombre() {
        return dptNombre;
    }

    public void setDptNombre(String dptNombre) {
        this.dptNombre = dptNombre;
    }

    public String getCiuNombre() {
        return ciuNombre;
    }

    public void setCiuNombre(String ciuNombre) {
        this.ciuNombre = ciuNombre;
    }

    public String getPrvCorreo() {
        return prvCorreo;
    }

    public void setPrvCorreo(String prvCorreo) {
        this.prvCorreo = prvCorreo;
    }

    public String getPrvToken() {
        return prvToken;
    }

    public String getPrvCarpeta() {
        return prvCarpeta;
    }

    public void setPrvCarpeta(String prvCarpeta) {
        this.prvCarpeta = prvCarpeta;
    }

    public void setPrvToken(String prvToken) {
        this.prvToken = prvToken;
    }

    public int getCrtId() {
        return crtId;
    }

    public void setCrtId(int crtId) {
        this.crtId = crtId;
    }

    public String getPrvEstado() {
        return prvEstado;
    }

    public void setPrvEstado(String prvEstado) {
        this.prvEstado = prvEstado;
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
