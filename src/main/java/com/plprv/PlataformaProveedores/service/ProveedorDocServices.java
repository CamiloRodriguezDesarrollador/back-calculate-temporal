package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IProveedorDocDao;
import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProveedorDocServices implements IProveedorDocServices {

    @Autowired
    private IProveedorDocDao proveedorDocDao;
    @Override
    @Transactional(readOnly = true)
    public ProveedorDoc encontrarProveedorDocsPorId(Integer prdId) {
        return (ProveedorDoc) proveedorDocDao.findByPrdId(prdId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDoc> encontrarProveedorDocsPorPrvId(Integer prvId) {
        return (List<ProveedorDoc>) proveedorDocDao.findByPrvId(prvId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDoc encontrarProveedorDocsPorFopId(Integer fopId , Integer prvId) {
        return (ProveedorDoc) proveedorDocDao.findByFopIdAndPrvId(fopId, prvId);
    }

    @Override
    public List<ProveedorDoc> encontrarProveedoresPorFopId(Integer fopId) {
        return (List<ProveedorDoc>) proveedorDocDao.findByFopId(fopId);
    }

    @Override
    public void crearProveedorDoc(ProveedorDoc proveedorDoc) {
        proveedorDocDao.save(proveedorDoc);
    }

    @Override
    public ProveedorDoc actualizarProveedorDoc(ProveedorDoc proveedorDoc) {
        return (ProveedorDoc) proveedorDocDao.save(proveedorDoc);

    }

    @Override
    public List<Object> encontrarTablaDocumental(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId,
                                                 Integer proId, Integer sprId, String prdEstadoDocumental , String tipo, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Object>) proveedorDocDao.cantidadDocumentacionTabla(prvId,crtId, forId,texto.toLowerCase(),prvNd,prvNombre.toLowerCase(), perId, proId, sprId, prdEstadoDocumental,tipo.toLowerCase(),numeroElementosPorPagina, limiteInicial);
    }

    @Override
    public List<Object> encontrarTablaEvaluacion(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Object>) proveedorDocDao.cantidadEvaluacionTabla(prvId,crtId, prvNd,  prvNombre.toLowerCase() , perId, proId, sprId, preEstado,numeroElementosPorPagina, limiteInicial);
    }

    @Override
    public Integer cantidadPaginasProveedorDoc(Integer prvId,Integer crtId, Integer forId, String texto, String prvNd, String prvNombre,Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo) {
        List<Object> datos = (List<Object>) proveedorDocDao.cantidadDocumentacionTablaTodo(prvId,crtId, forId, texto.toLowerCase(), prvNd,prvNombre.toLowerCase(), perId, proId, sprId, prdEstadoDocumental, tipo.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public Integer cantidadPaginasProveedorEva(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado) {
        List<Object> datos = (List<Object>) proveedorDocDao.cantidadEvaluacionTablaTodo(prvId,crtId, prvNd,  prvNombre.toLowerCase() , perId, proId, sprId, preEstado);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public List<Object> encontrarTablaDocumentalTodo(Integer prvId,Integer crtId,Integer forId, String texto, String prvNd, String prvNombre, Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo) {
        return (List<Object>) proveedorDocDao.cantidadDocumentacionTablaTodo(prvId, crtId,forId,texto.toLowerCase(),prvNd,prvNombre.toLowerCase(), perId, proId, sprId, prdEstadoDocumental , tipo.toLowerCase());
    }


    @Override
    public List<Object> descargarInformeGeneral(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado) {
        return (List<Object>) proveedorDocDao.informeGeneral(prvId,crtId, prvNd,  prvNombre.toLowerCase() , perId, proId, sprId, preEstado);
    }

}
