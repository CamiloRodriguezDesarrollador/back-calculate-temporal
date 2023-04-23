package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IPeriodoEvaluacionDao;
import com.plprv.PlataformaProveedores.dao.IProcesoDao;
import com.plprv.PlataformaProveedores.entity.Proceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProcesoServices implements IProcesoServices {

    @Autowired
    private IProcesoDao procesoDao;
    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesos(String proEstado) {
        return (List<Proceso>) procesoDao.findByProEstado(proEstado);
    }
    @Override
    @Transactional(readOnly = true)
    public Proceso encontrarProcesosPorNombre(String proNombre) {
        return (Proceso) procesoDao.findByProNombre(proNombre);
    }
    @Override
    @Transactional(readOnly = true)
    public Proceso encontrarProcesosPorId(Integer proId, String proEstado) {
        if (proEstado == null) {
            return procesoDao.findByProId(proId);
        } else {
            return procesoDao.findByProIdAndProEstado(proId, proEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadProcesos(String proEstado) {
        List<Proceso> datos = (List<Proceso>) procesoDao.findByProEstado(proEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesosNombres(String proEstado) {
        return (List<Proceso>) procesoDao.findByProEstadoNombre(proEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesosFiltro(String proEstado, String texto) {
        return (List<Proceso>) procesoDao.findByProEstadoFiltro(proEstado,texto.toLowerCase());
    }

    @Override
    public List<Proceso> encontrarProcesosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Proceso>) procesoDao.findByProEstadoPaginaFiltro(proEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearProceso(Proceso proceso) {
        procesoDao.save(proceso);
    }
    @Override
    public Integer cantidadPaginasProcesos(String proEstado, String texto) {
        List<Proceso> datos = (List<Proceso>) procesoDao.findByProEstadoFiltro(proEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Proceso borrarProceso(Proceso proceso) {
        return (Proceso) procesoDao.save(proceso);
    }
    @Override
    public Proceso actualizarProceso(Proceso proceso) {
        return (Proceso) procesoDao.save(proceso);

    }
}
