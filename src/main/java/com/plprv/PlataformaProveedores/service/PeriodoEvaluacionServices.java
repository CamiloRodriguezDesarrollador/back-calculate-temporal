package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IPeriodoEvaluacionDao;
import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PeriodoEvaluacionServices implements IPeriodoEvaluacionServices {

    @Autowired
    private IPeriodoEvaluacionDao periodoEvaluacionDao;
    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacions(String perEstado) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstado(perEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorNombre(String perNombre) {
        return (PeriodoEvaluacion) periodoEvaluacionDao.findByPerNombre(perNombre);
    }
    @Override
    @Transactional(readOnly = true)
    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorId(Integer perId, String perEstado) {
        if (perEstado == null) {
            return periodoEvaluacionDao.findByPerId(perId);
        } else {
            return periodoEvaluacionDao.findByPerIdAndPerEstado(perId, perEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadPeriodoEvaluacions(String perEstado) {
        List<PeriodoEvaluacion> datos = (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstado(perEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsNombres( String perTipo) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoNombre(perTipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltro(String perEstado, String texto) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoFiltro(perEstado,texto.toLowerCase());
    }

    @Override
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltroPaginas(String perEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoPaginaFiltro(perEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion) {
        periodoEvaluacionDao.save(periodoEvaluacion);
    }
    @Override
    public Integer cantidadPaginasPeriodoEvaluacions(String perEstado, String texto) {
        List<PeriodoEvaluacion> datos = (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoFiltro(perEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public PeriodoEvaluacion borrarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion) {
        return (PeriodoEvaluacion) periodoEvaluacionDao.save(periodoEvaluacion);
    }
    @Override
    public PeriodoEvaluacion actualizarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion) {
        return (PeriodoEvaluacion) periodoEvaluacionDao.save(periodoEvaluacion);

    }
}
