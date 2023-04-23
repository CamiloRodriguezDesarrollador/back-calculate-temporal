package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PLPRV_CLIENTE")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_EMPPAL")
    private int idEmppal;

    @Column(name = "TDC_TD" , length = 30)
    private String tdctd;

    @Column(name = "NIT_ND", length = 50)
    private String nitNd;

    @Column(name = "TDC_TD_GRUPOEMP" , length = 30)
    private String tdcTdGrupoemp;

    @Column(name = "NIT_ND_GRUPOEMP" ,length = 30)
    private String nitNdGrupoemp;

    @Column(name = "NIT_PRINCIPAL" ,length = 10)
    private String nitPrincipal;

    @Column(name = "NIT_NOMBRE" ,length = 250)
    private String nitNombre;

    @Column(name = "NIT_LOGO" ,length = 150)
    private String nitLogo;

    @Column(name = "PAI_NOMBRE" ,length = 30)
    private String paiNombre;

    @Column(name = "DPT_NOMBRE" ,length = 30)
    private String dptNombre;

    @Column(name = "CIU_NOMBRE" ,length = 30)
    private String ciuNombre;

    @Column(name = "NIT_DIRECCION" ,length = 100)
    private String nitDireccion;

    @Column(name = "NIT_TELEFONO" ,length = 30)
    private String nitTelefono;

    @Column(name = "NIT_COLORSIDE" ,length = 30)
    private String nitColorside;

    @Column(name = "NIT_COLORLETRA" ,length = 30)
    private String nitColorletra;

    @Column(name = "NIT_COLORFONDO" ,length = 30)
    private String nitColorfondo;

    @Column(name = "NIT_ESTADO" ,length = 10)
    private String nitEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    @PrePersist
    public void prePersist() {
        audFecha = new Date();
    }

    public int getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(int idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getTdctd() {
        return tdctd;
    }

    public void setTdctd(String tdctd) {
        this.tdctd = tdctd;
    }

    public String getNitNd() {
        return nitNd;
    }

    public void setNitNd(String nitNd) {
        this.nitNd = nitNd;
    }

    public String getTdcTdGrupoemp() {
        return tdcTdGrupoemp;
    }

    public void setTdcTdGrupoemp(String tdcTdGrupoemp) {
        this.tdcTdGrupoemp = tdcTdGrupoemp;
    }

    public String getNitNdGrupoemp() {
        return nitNdGrupoemp;
    }

    public void setNitNdGrupoemp(String nitNdGrupoemp) {
        this.nitNdGrupoemp = nitNdGrupoemp;
    }

    public String getNitPrincipal() {
        return nitPrincipal;
    }

    public void setNitPrincipal(String nitPrincipal) {
        this.nitPrincipal = nitPrincipal;
    }

    public String getNitNombre() {
        return nitNombre;
    }

    public void setNitNombre(String nitNombre) {
        this.nitNombre = nitNombre;
    }

    public String getNitLogo() {
        return nitLogo;
    }

    public void setNitLogo(String nitLogo) {
        this.nitLogo = nitLogo;
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

    public String getNitDireccion() {
        return nitDireccion;
    }

    public void setNitDireccion(String nitDireccion) {
        this.nitDireccion = nitDireccion;
    }

    public String getNitTelefono() {
        return nitTelefono;
    }

    public void setNitTelefono(String nitTelefono) {
        this.nitTelefono = nitTelefono;
    }

    public String getNitColorside() {
        return nitColorside;
    }

    public void setNitColorside(String nitColorside) {
        this.nitColorside = nitColorside;
    }

    public String getNitColorletra() {
        return nitColorletra;
    }

    public void setNitColorletra(String nitColorletra) {
        this.nitColorletra = nitColorletra;
    }

    public String getNitColorfondo() {
        return nitColorfondo;
    }

    public void setNitColorfondo(String nitColorfondo) {
        this.nitColorfondo = nitColorfondo;
    }

    public String getNitEstado() {
        return nitEstado;
    }

    public void setNitEstado(String nitEstado) {
        this.nitEstado = nitEstado;
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
