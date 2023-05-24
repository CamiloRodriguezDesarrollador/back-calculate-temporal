package com.plprv.PlataformaProveedores.entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_PROVEEDORDOC")
public class ProveedorDoc {

    @Id
    @Column(name = "PRD_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer prdId;
    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "PRV_ID")
    private int prvId;

    @Column(name = "FOP_ID")
    private int fopId;

    @Column(name = "PRD_DATA", length = 200)
    private String prdData;

    @Column(name = "PRD_OBSERVACION", length = 200)
    private String prdObservacion;
    @Column(name = "PRD_FECHA_DOCUMENTO", length = 50)
    private String prdFechaDocumento;

    @Column(name = "PRD_ESTADO_DOCUMENTAL", length = 30)
    private String prdEstadoDocumental;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;
    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
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

    public int getFopId() {
        return fopId;
    }

    public void setFopId(int fopId) {
        this.fopId = fopId;
    }

    public String getPrdData() {
        return prdData;
    }

    public void setPrdData(String prdData) {
        this.prdData = prdData;
    }

    public String getPrdFechaDocumento() {
        return prdFechaDocumento;
    }

    public void setPrdFechaDocumento(String prdFechaDocumento) {
        this.prdFechaDocumento = prdFechaDocumento;
    }

    public String getPrdEstadoDocumental() {
        return prdEstadoDocumental;
    }

    public String getPrdObservacion() {
        return prdObservacion;
    }

    public void setPrdObservacion(String prdObservacion) {
        this.prdObservacion = prdObservacion;
    }

    public void setPrdEstadoDocumental(String prdEstadoDocumental) {
        this.prdEstadoDocumental = prdEstadoDocumental;
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
