package com.plprv.PlataformaProveedores.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_FORMULARIOPROCESO")
public class FormularioProceso {

    @Id
    @Column(name = "FOP_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer fopId;

    @Column(name = "ID_EMPPAL", length = 30)
    private int idEmppal;

    @Column(name = "TDC_TD", length = 30)
    private String tdcTd;

    @Column(name = "FOR_ID")
    private int forId;

    @Column(name = "FOD_ID")
    private int fodId;

    @Column(name = "PRO_ID")
    private int proId;

    @Column(name = "PER_ID")
    private int perId;

    @Column(name = "SPR_ID")
    private int sprId;

    @Column(name = "CRT_ID")
    private int crtId;

    @Column(name = "FOP_ESTADO", length = 10)
    private String fopEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getFopId() {
        return fopId;
    }

    public void setFopId(Integer fopId) {
        this.fopId = fopId;
    }

    public int getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(int idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getTdcTd() {
        return tdcTd;
    }

    public void setTdcTd(String tdcTd) {
        this.tdcTd = tdcTd;
    }

    public int getForId() {
        return forId;
    }

    public void setForId(int forId) {
        this.forId = forId;
    }

    public int getFodId() {
        return fodId;
    }

    public void setFodId(int fodId) {
        this.fodId = fodId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public int getSprId() {
        return sprId;
    }

    public void setSprId(int sprId) {
        this.sprId = sprId;
    }

    public int getCrtId() {
        return crtId;
    }

    public void setCrtId(int crtId) {
        this.crtId = crtId;
    }

    public String getFopEstado() {
        return fopEstado;
    }

    public void setFopEstado(String fopEstado) {
        this.fopEstado = fopEstado;
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
