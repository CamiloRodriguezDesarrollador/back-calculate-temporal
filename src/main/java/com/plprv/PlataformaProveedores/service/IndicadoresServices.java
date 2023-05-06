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
    public Integer cantidadProveedoresTotal(Integer idEmppal) {
        return (Integer) indicadoresDao.encontrarCantidadProveedores(idEmppal);
    }

    @Override
    public Integer cantidadProcesosTotal(Integer idEmppal) {
        return (Integer) indicadoresDao.contarProcesos(idEmppal);

    }

    @Override
    public Integer cantidadPeriodosTotal(Integer idEmppal) {
        return (Integer) indicadoresDao.contarPeriodos(idEmppal);

    }

    @Override
    public List<Object>  obtenerTodaInformacionProveedorEva(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        return (List<Object>) indicadoresDao.obtenerInformacionProveedorCriticidadPeriodo(crtId,perId , proId, idEmppal);
    }

    @Override
    public List<Object> obtenerTodaInformacionTabla(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        return (List<Object>) indicadoresDao.contarRegistrosPorCriticidad(crtId,perId, proId, idEmppal);
    }

    @Override
    public Integer cantidadProveedoreFiltroTabla(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        return (Integer) indicadoresDao.cantidadProveedoresFiltro(crtId,perId, proId, idEmppal);
    }
    @Override
    public List<Object> obtenerProcesosFiltroTabla(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        return (List<Object>) indicadoresDao.contarProcesosPorFiltro(crtId,perId, proId, idEmppal);
    }

    @Override
    public List<Object> obtenerEstadoDocimentos(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        return (List<Object>) indicadoresDao.contarDocumentosPorFiltro(crtId,perId, proId, idEmppal);
    }

    @Override
    public Double porcentajeAvance(Integer crtId, Integer perId,Integer proId, Integer idEmppal) {
        Double resultado = (indicadoresDao.registrosCompleto(crtId,perId, proId, idEmppal) / indicadoresDao.totalRegistros(crtId,perId, proId, idEmppal))*100;
        return (Double) resultado;
    }

}
