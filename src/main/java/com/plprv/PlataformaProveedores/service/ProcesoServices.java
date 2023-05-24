package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IPeriodoEvaluacionDao;
import com.plprv.PlataformaProveedores.dao.IProcesoDao;
import com.plprv.PlataformaProveedores.entity.Proceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProcesoServices implements IProcesoServices {

    @Autowired
    private IProcesoDao procesoDao;
    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesos(String proEstado, Integer idEmppal) {
        return (List<Proceso>) procesoDao.findByProEstadoAndIdEmppalOrderByProNombreAsc(proEstado, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Proceso encontrarProcesosPorNombre(String proNombre, Integer idEmppal) {
        return (Proceso) procesoDao.findByProNombreAndIdEmppal(proNombre, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Proceso encontrarProcesosPorId(Integer proId, String proEstado, Integer idEmppal) {
        if (proEstado == null) {
            return procesoDao.findByProIdAndIdEmppal(proId, idEmppal);
        } else {
            return procesoDao.findByProIdAndProEstadoAndIdEmppal(proId, proEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadProcesos(String proEstado, Integer idEmppal) {
        List<Proceso> datos = (List<Proceso>) procesoDao.findByProEstadoAndIdEmppalOrderByProNombreAsc(proEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesosNombres(String proEstado, Integer idEmppal) {
        return (List<Proceso>) procesoDao.findByProEstadoNombre(proEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proceso> encontrarProcesosFiltro(String proEstado, String texto, Integer idEmppal) {
        return (List<Proceso>) procesoDao.findByProEstadoFiltro(proEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Proceso> encontrarProcesosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "proId");
        return (List<Proceso>) procesoDao.findByProEstadoPaginaFiltro(proEstado,texto.toLowerCase(), idEmppal, pageable);
    }

    @Override
    public void crearProceso(Proceso proceso) {
        procesoDao.save(proceso);
    }
    @Override
    public Integer cantidadPaginasProcesos(String proEstado, String texto, Integer idEmppal) {
        List<Proceso> datos = (List<Proceso>) procesoDao.findByProEstadoFiltro(proEstado,texto.toLowerCase(), idEmppal);
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
