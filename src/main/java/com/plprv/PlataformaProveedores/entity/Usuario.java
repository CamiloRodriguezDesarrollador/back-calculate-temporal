package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "PLPRV_USUARIO")
public class Usuario {

    @Id
    @Column(name = "USU_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer usuId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "USU_NOMBRE", length = 100)
    private String usuNombre;

    @Column(name = "USU_DOCUMENTO", length = 100)
    private String usuDocumento;
    @Column(name = "USU_CORREO", length = 100)
    private String usuCorreo;

    @Column(name = "USU_CONTRASENA", length = 150)
    private String usuContrasena;

    @Column(name = "USU_TIPO", length = 30)
    private String usuTipo;

    @Column(name = "USU_ROL", length = 30)
    private String usuRol;

    @Column(name = "USU_TOKEN", length = 255)
    private String usuToken;

    @Column(name = "USU_TOKENREFRESH", length = 255)
    private String usuTokenRefresh;

    @Column(name = "USU_ESTADOTOKEN", length = 30)
    private String usuEstadoToken;

    @Column(name = "USU_ESTADO", length = 10)
    private String usuEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuDocumento() {
        return usuDocumento;
    }

    public void setUsuDocumento(String usuDocumento) {
        this.usuDocumento = usuDocumento;
    }

    public String getUsuCorreo() {
        return usuCorreo;
    }

    public void setUsuCorreo(String usuCorreo) {
        this.usuCorreo = usuCorreo;
    }

    public String getUsuContrasena() {
        return usuContrasena;
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena = usuContrasena;
    }

    public String getUsuTipo() {
        return usuTipo;
    }

    public void setUsuTipo(String usuTipo) {
        this.usuTipo = usuTipo;
    }

    public String getUsuRol() {
        return usuRol;
    }

    public void setUsuRol(String usuRol) {
        this.usuRol = usuRol;
    }

    public String getUsuToken() {
        return usuToken;
    }

    public void setUsuToken(String usuToken) {
        this.usuToken = usuToken;
    }

    public String getUsuTokenRefresh() {
        return usuTokenRefresh;
    }

    public void setUsuTokenRefresh(String usuTokenRefresh) {
        this.usuTokenRefresh = usuTokenRefresh;
    }

    public String getUsuEstadoToken() {
        return usuEstadoToken;
    }

    public void setUsuEstadoToken(String usuEstadoToken) {
        this.usuEstadoToken = usuEstadoToken;
    }

    public String getUsuEstado() {
        return usuEstado;
    }

    public void setUsuEstado(String usuEstado) {
        this.usuEstado = usuEstado;
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
