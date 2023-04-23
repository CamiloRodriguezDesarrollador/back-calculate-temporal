package com.plprv.PlataformaProveedores.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class InformeGeneral {

    private Integer idPeriodo;

    private Integer idProveedor;

    private String numeroDocumentoProveedor;

    private String tipoDocumentoProveedor;

    private String nombreProveedor;

    private String nombrePeriodo;

    private String fechasPeriodo;

    private String tipoPeriodo;

    private String nombreProceso;

    private String subProcesoNombre;

    private Date fechaCreacion;
    private String usuarioCreacion;

    private Long diasVigencias;

    private String criticidad;
    private String direccionProveedor;
    private String celularProveedor;
    private String ciudadProveedor;
    private String departamentoProveedor;
    private String correoProveedor;
    private Date fechaCreacionProveedor;
    private String carpetaProveedor;

    private String estadoPeriodoEvaluacion;
    private String continuaPeriodo;
    private String observacionPeriodo;
    private Integer resultadoPeriodo;

    public InformeGeneral( Integer idPeriodo, Integer idProveedor, String tipoDocumentoProveedor , String numeroDocumentoProveedor, String nombreProveedor,
                          String nombrePeriodo, String fechasPeriodo, String tipoPeriodo, String nombreProceso, String subProcesoNombre, Date fechaCreacion, String usuarioCreacion ,  String criticidad
                    , String direccionProveedor,String celularProveedor,String ciudadProveedor,String departamentoProveedor,String correoProveedor,
                          String carpetaProveedor,Date fechaCreacionProveedor, String estadoPeriodoEvaluacion,String continuaPeriodo,String observacionPeriodo,Integer resultadoPeriodo) {
        this.idPeriodo = idPeriodo;
        this.idProveedor = idProveedor;
        this.tipoDocumentoProveedor = tipoDocumentoProveedor;
        this.numeroDocumentoProveedor = numeroDocumentoProveedor;
        this.nombreProveedor = nombreProveedor;
        this.nombrePeriodo = nombrePeriodo;
        this.fechasPeriodo = fechasPeriodo;
        this.tipoPeriodo = tipoPeriodo;
        this.nombreProceso = nombreProceso;
        this.subProcesoNombre = subProcesoNombre;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.criticidad = criticidad;
        this.diasVigencias = 0L;
        this.direccionProveedor = direccionProveedor;
        this.celularProveedor = celularProveedor;
        this.ciudadProveedor =ciudadProveedor;
        this.departamentoProveedor =departamentoProveedor;
        this.correoProveedor =correoProveedor;
        this.fechaCreacionProveedor =fechaCreacionProveedor;
        this.carpetaProveedor =carpetaProveedor;
        this.estadoPeriodoEvaluacion =estadoPeriodoEvaluacion;
        this.continuaPeriodo = continuaPeriodo;
        this.observacionPeriodo = observacionPeriodo;
        this.resultadoPeriodo = resultadoPeriodo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNumeroDocumentoProveedor() {
        return numeroDocumentoProveedor;
    }

    public void setNumeroDocumentoProveedor(String numeroDocumentoProveedor) {
        this.numeroDocumentoProveedor = numeroDocumentoProveedor;
    }

    public String getTipoDocumentoProveedor() {
        return tipoDocumentoProveedor;
    }

    public void setTipoDocumentoProveedor(String tipoDocumentoProveedor) {
        this.tipoDocumentoProveedor = tipoDocumentoProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }



    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public String getFechasPeriodo() {
        return fechasPeriodo;
    }

    public void setFechasPeriodo(String fechasPeriodo) {
        this.fechasPeriodo = fechasPeriodo;
    }

    public String getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(String tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getSubProcesoNombre() {
        return subProcesoNombre;
    }

    public void setSubProcesoNombre(String subProcesoNombre) {
        this.subProcesoNombre = subProcesoNombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Long getDiasVigencias() {
        return diasVigencias;
    }

    public void setDiasVigencias(Long diasVigencias) {
        this.diasVigencias = diasVigencias;
    }

    public String getCriticidad() {
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }


    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getCelularProveedor() {
        return celularProveedor;
    }

    public void setCelularProveedor(String celularProveedor) {
        this.celularProveedor = celularProveedor;
    }

    public String getCiudadProveedor() {
        return ciudadProveedor;
    }

    public void setCiudadProveedor(String ciudadProveedor) {
        this.ciudadProveedor = ciudadProveedor;
    }

    public String getDepartamentoProveedor() {
        return departamentoProveedor;
    }

    public void setDepartamentoProveedor(String departamentoProveedor) {
        this.departamentoProveedor = departamentoProveedor;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public Date getFechaCreacionProveedor() {
        return fechaCreacionProveedor;
    }

    public void setFechaCreacionProveedor(Date fechaCreacionProveedor) {
        this.fechaCreacionProveedor = fechaCreacionProveedor;
    }

    public String getCarpetaProveedor() {
        return carpetaProveedor;
    }

    public void setCarpetaProveedor(String carpetaProveedor) {
        this.carpetaProveedor = carpetaProveedor;
    }

    public String getContinuaPeriodo() {
        return continuaPeriodo;
    }

    public void setContinuaPeriodo(String continuaPeriodo) {
        this.continuaPeriodo = continuaPeriodo;
    }

    public String getObservacionPeriodo() {
        return observacionPeriodo;
    }

    public void setObservacionPeriodo(String observacionPeriodo) {
        this.observacionPeriodo = observacionPeriodo;
    }

    public Integer getResultadoPeriodo() {
        return resultadoPeriodo;
    }

    public void setResultadoPeriodo(Integer resultadoPeriodo) {
        this.resultadoPeriodo = resultadoPeriodo;
    }

    public String getEstadoPeriodoEvaluacion() {
        return estadoPeriodoEvaluacion;
    }

    public void setEstadoPeriodoEvaluacion(String estadoPeriodoEvaluacion) {
        this.estadoPeriodoEvaluacion = estadoPeriodoEvaluacion;
    }

    public long calcularDias(String fechaDocumento, String vigencia){

        long diasVigencia = 0L;
        LocalDate fecha;
        LocalDate hoy;

        switch (vigencia){
            case "1":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia =365- ChronoUnit.DAYS.between(fecha, hoy);
                break;
            case "2":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia =730- ChronoUnit.DAYS.between(fecha, hoy);
                break;

            case "3":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia =1095- ChronoUnit.DAYS.between(fecha, hoy);
                break;

            case "4":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia =1460- ChronoUnit.DAYS.between(fecha, hoy);
                break;

            case "5":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia = 1825- ChronoUnit.DAYS.between(fecha, hoy);
                break;

            default:
                diasVigencias = 0L;
        }
        return diasVigencia;
    }
}
