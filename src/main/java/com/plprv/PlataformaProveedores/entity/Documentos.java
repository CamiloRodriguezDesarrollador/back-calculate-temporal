package com.plprv.PlataformaProveedores.entity;

import com.plprv.PlataformaProveedores.dao.IProveedorDocDao;
import com.plprv.PlataformaProveedores.service.IProveedorDocServices;
import com.plprv.PlataformaProveedores.service.ProveedorDocServices;
import org.hibernate.boot.archive.scan.internal.ScanResultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Documentos {

    private Integer prdId;
    private String dato;
    private String observacion;

    private String fechaDocumento;

    private String estadoDocumental;

    private String numeroDocumentoProveedor;

    private String tipoDocumentoProveedor;

    private String nombreProveedor;

    private String nombreFormulario;

    private String nombreCampo;

    private String tipoCampo;

    private String nombrePeriodo;

    private String fechasPeriodo;

    private String tipoPeriodo;

    private String nombreProceso;

    private String subProcesoNombre;

    private Date fechaCreacion;
    private String usuarioCreacion;

    private Long diasVigencias;

    private String estadoPeriodo;

    private String criticidad;

    private String vigenciaEstablecida;

    private String proveedorCorreo;

    private String estado;


    public Documentos(Integer prdId, String dato, String observacion, String fechaDocumento, String estadoDocumental , String tipoDocumentoProveedor , String numeroDocumentoProveedor, String nombreProveedor, String nombreFormulario, String nombreCampo, String tipoCampo,
                      String nombrePeriodo, String fechasPeriodo, String tipoPeriodo, String nombreProceso, String subProcesoNombre, Date fechaCreacion, String usuarioCreacion , String estadoPeriodo , String criticidad
                    , String vigenciaEstablecida, String proveedorCorreo) {
        this.prdId = prdId;
        this.dato = dato;
        if(tipoCampo.equals("file") && dato != null && !dato.equals("") ) this.dato = "https://drive.google.com/file/d/"+dato+"/view";
        this.observacion = observacion;
        this.fechaDocumento = fechaDocumento;
        this.estadoDocumental = estadoDocumental;
        this.tipoDocumentoProveedor = tipoDocumentoProveedor;
        this.numeroDocumentoProveedor = numeroDocumentoProveedor;
        this.nombreProveedor = nombreProveedor;
        this.nombreFormulario = nombreFormulario;
        this.nombreCampo = nombreCampo;
        this.tipoCampo = tipoCampo;
        this.estado = "A";
        if(tipoCampo.equals("text")) this.tipoCampo = "Texto";
        if(tipoCampo.equals("number")) this.tipoCampo = "Numero";
        if(tipoCampo.equals("file")) this.tipoCampo = "Archivo";
        if(tipoCampo.equals("select")) this.tipoCampo = "Escala";
        if(tipoCampo.equals("check")) this.tipoCampo = "Si/No";
        this.nombrePeriodo = nombrePeriodo;
        this.fechasPeriodo = fechasPeriodo;
        this.tipoPeriodo = tipoPeriodo;
        this.nombreProceso = nombreProceso;
        this.subProcesoNombre = subProcesoNombre;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.estadoPeriodo = estadoPeriodo;
        this.proveedorCorreo = proveedorCorreo;
        this.criticidad = criticidad;
            this.vigenciaEstablecida = vigenciaEstablecida;
        this.diasVigencias = 0L;
        if(this.dato == null) return;
        if(this.dato == null) return;
        if(this.dato.equals("") || this.dato.equals("NA")) return;
        if(this.fechaDocumento == null) return;
        if(this.fechaDocumento.equals("") || this.fechaDocumento.equals("NA")) return;
        this.diasVigencias = calcularDias(this.fechaDocumento , this.vigenciaEstablecida);
        String miEstado = "";
        if(this.diasVigencias >=0 && this.diasVigencias<=30 ) miEstado = "A-1";
        else if(this.diasVigencias >30 && this.diasVigencias<=90 ) miEstado = "A-3";
        else if(this.diasVigencias >90 ) miEstado = "A";
        else if(this.diasVigencias <0 ) miEstado = "V";
        if (!estadoDocumental.equals(miEstado)){
            this.estadoDocumental = miEstado;
            this.estado = "D";
        }
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getprdId() {
        return prdId;
    }

    public void setprdId(Integer prdId) {
        this.prdId = prdId;
    }

    public String getProveedorCorreo() {
        return proveedorCorreo;
    }

    public void setProveedorCorreo(String proveedorCorreo) {
        this.proveedorCorreo = proveedorCorreo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getEstadoDocumental() {
        return estadoDocumental;
    }

    public void setEstadoDocumental(String estadoDocumental) {
        this.estadoDocumental = estadoDocumental;
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

    public String getNombreFormulario() {
        return nombreFormulario;
    }

    public void setNombreFormulario(String nombreFormulario) {
        this.nombreFormulario = nombreFormulario;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
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

    public String getEstadoPeriodo() {
        return estadoPeriodo;
    }

    public void setEstadoPeriodo(String estadoPeriodo) {
        this.estadoPeriodo = estadoPeriodo;
    }

    public String getCriticidad() {
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }

    public String getVigenciaEstablecida() {
        return vigenciaEstablecida;
    }

    public void setVigenciaEstablecida(String vigenciaEstablecida) {
        this.vigenciaEstablecida = vigenciaEstablecida;
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

            case "10":
                fecha = LocalDate.parse(fechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                hoy = LocalDate.now();
                diasVigencia = 3650- ChronoUnit.DAYS.between(fecha, hoy);
                break;

            default:
                diasVigencias = 0L;
        }
        return diasVigencia;
    }


}
