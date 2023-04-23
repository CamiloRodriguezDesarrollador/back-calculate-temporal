package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IIndicadoresDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicadoresServices implements IIndicadoresServices {

    @Autowired
    private IIndicadoresDao indicadoresDao;


    @Override
    public Integer cantidadProveedoresTotal() {
        return (Integer) indicadoresDao.encontrarCantidadProveedores();
    }

    @Override
    public Integer cantidadProcesosTotal() {
        return (Integer) indicadoresDao.contarProcesos();

    }

    @Override
    public Integer cantidadPeriodosTotal() {
        return (Integer) indicadoresDao.contarPeriodos();

    }

    @Override
    public List<Object>  obtenerTodaInformacionProveedorEva(Integer crtId, Integer perId,Integer proId) {
        return (List<Object>) indicadoresDao.obtenerInformacionProveedorCriticidadPeriodo(crtId,perId , proId);
    }

    @Override
    public List<Object> obtenerTodaInformacionTabla(Integer crtId, Integer perId,Integer proId) {
        return (List<Object>) indicadoresDao.contarRegistrosPorCriticidad(crtId,perId, proId);
    }

    @Override
    public Integer cantidadProveedoreFiltroTabla(Integer crtId, Integer perId,Integer proId) {
        return (Integer) indicadoresDao.cantidadProveedoresFiltro(crtId,perId, proId);
    }
    @Override
    public List<Object> obtenerProcesosFiltroTabla(Integer crtId, Integer perId,Integer proId) {
        return (List<Object>) indicadoresDao.contarProcesosPorFiltro(crtId,perId, proId);
    }

    @Override
    public List<Object> obtenerEstadoDocimentos(Integer crtId, Integer perId,Integer proId) {
        return (List<Object>) indicadoresDao.contarDocumentosPorFiltro(crtId,perId, proId);
    }

    @Override
    public Double porcentajeAvance(Integer crtId, Integer perId,Integer proId) {
        Double resultado = (indicadoresDao.registrosCompleto(crtId,perId, proId) / indicadoresDao.totalRegistros(crtId,perId, proId))*100;
        return (Double) resultado;
    }

}
