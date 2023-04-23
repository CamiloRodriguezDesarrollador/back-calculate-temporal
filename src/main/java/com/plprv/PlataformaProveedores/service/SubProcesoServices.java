package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.ISubProcesoDao;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubProcesoServices implements ISubProcesoServices {

    @Autowired
    private ISubProcesoDao subProcesoDao;
    @Override
    @Transactional(readOnly = true)
    public List<SubProceso> encontrarSubProcesos(String proEstado) {
        return (List<SubProceso>) subProcesoDao.findBySprEstado(proEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public SubProceso encontrarSubProcesosPorNombre(String sprNombre, Integer proId) {
        return (SubProceso) subProcesoDao.findBySprNombreAndProId(sprNombre, proId);
    }
    @Override
    @Transactional(readOnly = true)
    public SubProceso encontrarSubProcesosPorId(Integer proId, String sprEstado) {
        if (sprEstado == null) {
            return subProcesoDao.findBySprId(proId);
        } else {
            return subProcesoDao.findBySprIdAndSprEstado(proId, sprEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadSubProcesos(String sprEstado) {
        List<SubProceso> datos = (List<SubProceso>) subProcesoDao.findBySprEstado(sprEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<SubProceso> encontrarSubProcesosNombres(String sprEstado) {
        return (List<SubProceso>) subProcesoDao.findBySprEstadoNombre(sprEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubProceso> encontrarSubProcesosFiltro(String sprEstado, String texto, Integer proId) {
        return (List<SubProceso>) subProcesoDao.findBySprEstadoFiltro(sprEstado,texto.toLowerCase(), proId);
    }

    @Override
    public List<SubProceso> encontrarSubProcesosFiltroPaginas(String sprEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina , Integer proId) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);

        List<SubProceso> miSubProceso = (List<SubProceso>) subProcesoDao.findBySprEstadoPaginaFiltro(sprEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial , proId);
        return miSubProceso;
    }

    @Override
    public void crearSubProceso(SubProceso subProceso) {
        subProcesoDao.save(subProceso);
    }
    @Override
    public Integer cantidadPaginasSubProcesos(String sprEstado, String texto , Integer proId) {

        List<SubProceso> datos = (List<SubProceso>) subProcesoDao.findBySprEstadoFiltro(sprEstado,texto.toLowerCase() , proId);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public SubProceso borrarSubProceso(SubProceso subProceso) {
        return (SubProceso) subProcesoDao.save(subProceso);
    }
    @Override
    public SubProceso actualizarSubProceso(SubProceso subProceso) {
        return (SubProceso) subProcesoDao.save(subProceso);

    }
}
