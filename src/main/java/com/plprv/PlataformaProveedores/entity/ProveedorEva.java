package com.plprv.PlataformaProveedores.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_PROVEEDOREVA")
public class ProveedorEva {

    @Id
    @Column(name = "PRE_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer preId;
    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "PRV_ID")
    private int prvId;

    @Column(name = "PER_ID")
    private int perId;

    @Column(name = "PRE_RESULTADO")
    private Integer preResultado;

    @Column(name = "PRE_OBSERVACION")
    private String preObservacion;

    @Column(name = "PRE_CONTINUA")
    private String preContinua;

    @Column(name = "PRE_ESTADO" , length = 10)
    private String preEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;
    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;


    public Integer getPreId() {
        return preId;
    }

    public void setPreId(Integer preId) {
        this.preId = preId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public int getPrvId() {
        return prvId;
    }

    public void setPrvId(int prvId) {
        this.prvId = prvId;
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public Integer getPreResultado() {
        return preResultado;
    }

    public void setPreResultado(Integer preResultado) {
        this.preResultado = preResultado;
    }

    public String getPreObservacion() {
        return preObservacion;
    }

    public void setPreObservacion(String preObservacion) {
        this.preObservacion = preObservacion;
    }

    public String getPreContinua() {
        return preContinua;
    }

    public void setPreContinua(String preContinua) {
        this.preContinua = preContinua;
    }

    public String getPreEstado() {
        return preEstado;
    }

    public void setPreEstado(String preEstado) {
        this.preEstado = preEstado;
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
