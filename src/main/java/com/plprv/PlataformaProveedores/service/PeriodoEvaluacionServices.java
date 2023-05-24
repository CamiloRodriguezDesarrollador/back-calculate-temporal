package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IPeriodoEvaluacionDao;
import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PeriodoEvaluacionServices implements IPeriodoEvaluacionServices {

    @Autowired
    private IPeriodoEvaluacionDao periodoEvaluacionDao;
    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacions(String perEstado, Integer idEmppal) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoAndIdEmppal(perEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorNombre(String perNombre, Integer idEmppal) {
        return (PeriodoEvaluacion) periodoEvaluacionDao.findByPerNombreAndIdEmppal(perNombre, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorId(Integer perId, String perEstado, Integer idEmppal) {
        if (perEstado == null) {
            return periodoEvaluacionDao.findByPerIdAndIdEmppal(perId, idEmppal);
        } else {
            return periodoEvaluacionDao.findByPerIdAndPerEstadoAndIdEmppal(perId, perEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadPeriodoEvaluacions(String perEstado, Integer idEmppal) {
        List<PeriodoEvaluacion> datos = (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoAndIdEmppal(perEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsNombres( String perTipo, Integer idEmppal) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoNombre(perTipo, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltro(String perEstado, String texto, Integer idEmppal) {
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoFiltro(perEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltroPaginas(String perEstado, String texto, Integer numeroDePagina,
                                                                            Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "perId");
        return (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoPaginaFiltro(perEstado,texto.toLowerCase(), idEmppal , pageable);
    }

    @Override
    public void crearPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion) {
        periodoEvaluacionDao.save(periodoEvaluacion);
    }
    @Override
    public Integer cantidadPaginasPeriodoEvaluacions(String perEstado, String texto, Integer idEmppal) {
        List<PeriodoEvaluacion> datos = (List<PeriodoEvaluacion>) periodoEvaluacionDao.findByPerEstadoFiltro(perEstado,texto.toLowerCase(), idEmppal);
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
