package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IProveedorEvaDao;
import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorEvaServices implements IProveedorEvaServices {

    @Autowired
    private IProveedorEvaDao proveedorEvaDao;
    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaPorId(Integer preId) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreId(preId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaPorPrvId(Integer prvId) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPrvId(prvId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEva encontrarProveedorEvaPorPerId(Integer perId , Integer prvId) {
        return (ProveedorEva) proveedorEvaDao.findByPerIdAndPrvId(perId, prvId);
    }
    @Override
    public void crearProveedorEva(ProveedorEva proveedorEva) {
        proveedorEvaDao.save(proveedorEva);
    }

    @Override
    public ProveedorEva actualizarProveedorEva(ProveedorEva proveedorEva) {
        return (ProveedorEva) proveedorEvaDao.save(proveedorEva);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEva(String preEstado) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstado(preEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEva encontrarProveedorEvaPorId(Integer perId, String preEstado) {
        if (preEstado == null) {
            return proveedorEvaDao.findByPreId(perId);
        } else {
            return proveedorEvaDao.findByPreIdAndPreEstado(perId, preEstado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaPorFormulario(Integer perId, String preEstado) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPerIdAndPreEstado(perId, preEstado);
    }
    @Transactional(readOnly = true)
    public Integer cantidadProveedorEva(String preEstado) {
        List<ProveedorEva> datos = (List<ProveedorEva>) proveedorEvaDao.findByPreEstado(preEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public List<DocumentosProveedor> encontrarProveedoresEstados(Integer perId) {
        return (List<DocumentosProveedor>) proveedorEvaDao.encontrarProveedoresEstadosCalcular(perId);
    }

    @Override
    public Long encontrarCantidadRequerida(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd) {
        return (Long) proveedorEvaDao.encontrarCantidadFormulario(perId,proId,sprId,crtId,tdcTd);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaFiltro(String preEstado, String texto, Integer perId) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoFiltro(preEstado,texto.toLowerCase(),perId);
    }

    @Override
    public List<ProveedorEva> encontrarProveedorEvaFiltroPaginas(String preEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer perId) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoPaginaFiltro(preEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial,perId);
    }

    @Override
    public Integer cantidadPaginasProveedorEva(String preEstado, String texto, Integer perId) {
        List<ProveedorEva> datos = (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoFiltro(preEstado,texto.toLowerCase(), perId);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public ProveedorEva borrarProveedorEva(ProveedorEva proveedorEva) {
        return (ProveedorEva) proveedorEvaDao.save(proveedorEva);
    }


}
