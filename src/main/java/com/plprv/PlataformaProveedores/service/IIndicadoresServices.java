package com.plprv.PlataformaProveedores.service;


import java.util.List;

public interface IIndicadoresServices {

    public Integer cantidadProveedoresTotal();
    public Integer cantidadPeriodosTotal();

    public Integer cantidadProcesosTotal();


    public List<Object>  obtenerTodaInformacionProveedorEva(Integer crtId , Integer perId ,Integer proId);

    public List<Object> obtenerTodaInformacionTabla(Integer crtId , Integer perId,Integer proId);

    public Integer cantidadProveedoreFiltroTabla(Integer crtId , Integer perId,Integer proId);

    public List<Object> obtenerProcesosFiltroTabla(Integer crtId , Integer perId,Integer proId);

    public List<Object> obtenerEstadoDocimentos(Integer crtId , Integer perId,Integer proId);

    public Double porcentajeAvance(Integer crtId , Integer perId,Integer proId);

}