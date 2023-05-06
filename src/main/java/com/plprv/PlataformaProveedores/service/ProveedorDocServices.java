package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IProveedorDocDao;
import com.plprv.PlataformaProveedores.entity.Documentos;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorDocServices implements IProveedorDocServices {

    @Autowired
    private IProveedorDocDao proveedorDocDao;

    @Override
    @Transactional(readOnly = true)
    public ProveedorDoc encontrarProveedorDocsPorId(Integer prdId, Integer idEmppal) {
        return (ProveedorDoc) proveedorDocDao.findByPrdIdAndIdEmppal(prdId, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDoc> encontrarProveedorDocsPorPrvId(Integer prvId, Integer idEmppal) {
        return (List<ProveedorDoc>) proveedorDocDao.findByPrvIdAndIdEmppal(prvId, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDoc encontrarProveedorDocsPorFopId(Integer fopId , Integer prvId, Integer idEmppal) {
        return (ProveedorDoc) proveedorDocDao.findByFopIdAndPrvIdAndIdEmppal(fopId, prvId, idEmppal);
    }

    @Override
    public List<ProveedorDoc> encontrarProveedoresPorFopId(Integer fopId, Integer idEmppal) {
        return (List<ProveedorDoc>) proveedorDocDao.findByFopIdAndIdEmppal(fopId, idEmppal);
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
                                                 Integer proId, Integer sprId, String prdEstadoDocumental , String tipo, Integer numeroDePagina,
                                                 Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Object>) proveedorDocDao.cantidadDocumentacionTabla(prvId,crtId, forId,texto.toLowerCase(),prvNd,prvNombre.toLowerCase(),
                perId, proId, sprId, prdEstadoDocumental,tipo.toLowerCase(),numeroElementosPorPagina, limiteInicial, idEmppal);
    }

    @Override
    public List<Object> encontrarTablaEvaluacion(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId,
                                                 Integer proId, Integer sprId, String preEstado, Integer numeroDePagina,
                                                 Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Object>) proveedorDocDao.cantidadEvaluacionTabla(prvId,crtId, prvNd,  prvNombre.toLowerCase() , perId,
                proId, sprId, preEstado,numeroElementosPorPagina, limiteInicial, idEmppal);
    }

    @Override
    public Integer cantidadPaginasProveedorDoc(Integer prvId,Integer crtId, Integer forId, String texto, String prvNd,
                                               String prvNombre,Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal) {
        List<Documentos> datos = (List<Documentos>) proveedorDocDao.cantidadDocumentacionTablaTodo(prvId,crtId, forId, texto.toLowerCase(), prvNd,
                prvNombre.toLowerCase(), perId, proId, sprId, prdEstadoDocumental, tipo.toLowerCase(), idEmppal);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public Integer cantidadPaginasProveedorEva(Integer prvId,Integer crtId, String prvNd, String prvNombre ,
                                               Integer perId, Integer proId, Integer sprId, String preEstado, Integer idEmppal) {
        List<Object> datos = (List<Object>) proveedorDocDao.cantidadEvaluacionTablaTodo(prvId,crtId, prvNd,  prvNombre.toLowerCase()
                , perId, proId, sprId, preEstado, idEmppal);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public void actualizarTodosEstado(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre,
                                      Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal) {
        List<Documentos> misDocumentos =  proveedorDocDao.cantidadDocumentacionTablaTodo(prvId, crtId,forId,texto.toLowerCase(),prvNd,
                prvNombre.toLowerCase(), perId, proId, sprId, prdEstadoDocumental , tipo.toLowerCase(), idEmppal);
        for (Documentos misD : misDocumentos){
            if (misD.getEstado().equals("D")){
                ProveedorDoc prvd = proveedorDocDao.findByPrdIdAndIdEmppal(misD.getprdId(), idEmppal);
                prvd.setPrdEstadoDocumental(misD.getEstadoDocumental());
                proveedorDocDao.save(prvd);
            }
        }

    }

    @Override
    public List<Documentos> encontrarTablaDocumentalTodo(Integer prvId,Integer crtId,Integer forId, String texto, String prvNd, String prvNombre,
                                                         Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal) {

        return (List<Documentos>) proveedorDocDao.cantidadDocumentacionTablaTodo(prvId, crtId,forId,texto.toLowerCase(),prvNd,prvNombre.toLowerCase(),
                perId, proId, sprId, prdEstadoDocumental , tipo.toLowerCase(), idEmppal);
    }


    @Override
    public List<Object> descargarInformeGeneral(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId,
                                                Integer sprId, String preEstado, Integer idEmppal) {
        return (List<Object>) proveedorDocDao.informeGeneral(prvId,crtId, prvNd,  prvNombre.toLowerCase() , perId, proId, sprId, preEstado, idEmppal);
    }

}
