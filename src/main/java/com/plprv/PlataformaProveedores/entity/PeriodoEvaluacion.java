package com.plprv.PlataformaProveedores.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PLPRV_PERIODOEVALUACION")
public class PeriodoEvaluacion {

    @Id
    @Column(name = "PER_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int perId;

    @Column(name = "ID_EMPPAL", length = 30)
    private Integer idEmppal;

    @Column(name = "PER_NOMBRE", length = 50)
    private String perNombre;

    @Column(name = "PER_FECHAEVALUACION", length = 30)
    private String perFechaEvaluacion;

    @Column(name = "PER_VISIBILIDAD", length = 30)
    private String perVisibilidad;

    @Column(name = "PER_TIPO", length = 30)
    private String perTipo;

    @Column(name = "PER_ESTADO", length = 10)
    private String perEstado;

    @Column(name = "AUD_FECHA" ,length = 10)
    @Temporal(TemporalType.DATE)
    private Date audFecha;

    @Column(name = "AUD_USUARIO" ,length = 10)
    private String audUsuario;

    public void prePersist() {
        audFecha = new Date();
    }

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public String getPerNombre() {
        return perNombre;
    }

    public void setPerNombre(String perNombre) {
        this.perNombre = perNombre;
    }

    public String getPerFechaEvaluacion() {
        return perFechaEvaluacion;
    }

    public void setPerFechaEvaluacion(String perFechaEvaluacion) {
        this.perFechaEvaluacion = perFechaEvaluacion;
    }

    public String getPerVisibilidad() {
        return perVisibilidad;
    }

    public void setPerVisibilidad(String perVisibilidad) {
        this.perVisibilidad = perVisibilidad;
    }

    public String getPerTipo() {
        return perTipo;
    }

    public void setPerTipo(String perTipo) {
        this.perTipo = perTipo;
    }

    public String getPerEstado() {
        return perEstado;
    }

    public void setPerEstado(String perEstado) {
        this.perEstado = perEstado;
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
