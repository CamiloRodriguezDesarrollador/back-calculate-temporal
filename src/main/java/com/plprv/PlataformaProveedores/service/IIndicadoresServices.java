package com.plprv.PlataformaProveedores.service;


import java.util.List;

public interface IIndicadoresServices {

    public Integer cantidadProveedoresTotal(Integer idEmppal);
    public Integer cantidadPeriodosTotal(Integer idEmppal);

    public Integer cantidadProcesosTotal(Integer idEmppal);


    public List<Object>  obtenerTodaInformacionProveedorEva(Integer crtId , Integer perId ,Integer proId, Integer idEmppal);

    public List<Object> obtenerTodaInformacionTabla(Integer crtId , Integer perId,Integer proId, Integer idEmppal);

    public Integer cantidadProveedoreFiltroTabla(Integer crtId , Integer perId,Integer proId, Integer idEmppal);

    public List<Object> obtenerProcesosFiltroTabla(Integer crtId , Integer perId,Integer proId, Integer idEmppal);

    public List<Object> obtenerEstadoDocimentos(Integer crtId , Integer perId,Integer proId, Integer idEmppal);

    public Double porcentajeAvance(Integer crtId , Integer perId,Integer proId, Integer idEmppal);

}