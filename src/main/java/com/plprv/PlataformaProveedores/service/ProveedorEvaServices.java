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
    public List<ProveedorEva> encontrarProveedorEvaPorId(Integer preId, Integer idEmppal) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreIdAndIdEmppal(preId, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaPorPrvId(Integer prvId, Integer idEmppal) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPrvIdAndIdEmppal(prvId, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEva encontrarProveedorEvaPorPerId(Integer perId , Integer prvId, Integer idEmppal) {
        return (ProveedorEva) proveedorEvaDao.findByPerIdAndPrvIdAndIdEmppal(perId, prvId, idEmppal);
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
    public List<ProveedorEva> encontrarProveedorEva(String preEstado, Integer idEmppal) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoAndIdEmppal(preEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEva encontrarProveedorEvaPorId(Integer perId, String preEstado, Integer idEmppal) {
        if (preEstado == null) {
            return proveedorEvaDao.findByPreIdAndIdEmppal(perId, idEmppal);
        } else {
            return proveedorEvaDao.findByPreIdAndPreEstadoAndIdEmppal(perId, preEstado, idEmppal);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaPorFormulario(Integer perId, String preEstado, Integer idEmppal) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPerIdAndPreEstadoAndIdEmppal(perId, preEstado, idEmppal);
    }
    @Transactional(readOnly = true)
    public Integer cantidadProveedorEva(String preEstado, Integer idEmppal) {
        List<ProveedorEva> datos = (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoAndIdEmppal(preEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }

    @Override
    public List<DocumentosProveedor> encontrarProveedoresEstados(Integer perId, Integer idEmppal) {
        return (List<DocumentosProveedor>) proveedorEvaDao.encontrarProveedoresEstadosCalcular(perId, idEmppal);
    }

    @Override
    public Long encontrarCantidadRequerida(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd, Integer idEmppal) {
        return (Long) proveedorEvaDao.encontrarCantidadFormulario(perId,proId,sprId,crtId,tdcTd, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEva> encontrarProveedorEvaFiltro(String preEstado, String texto, Integer perId, Integer idEmppal) {
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoFiltro(preEstado,texto.toLowerCase(),perId, idEmppal);
    }

    @Override
    public List<ProveedorEva> encontrarProveedorEvaFiltroPaginas(String preEstado, String texto, Integer numeroDePagina,
                                                                 Integer numeroElementosPorPagina, Integer perId, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoPaginaFiltro(preEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial,perId, idEmppal);
    }

    @Override
    public Integer cantidadPaginasProveedorEva(String preEstado, String texto, Integer perId, Integer idEmppal) {
        List<ProveedorEva> datos = (List<ProveedorEva>) proveedorEvaDao.findByPreEstadoFiltro(preEstado,texto.toLowerCase(), perId, idEmppal);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public ProveedorEva borrarProveedorEva(ProveedorEva proveedorEva) {
        return (ProveedorEva) proveedorEvaDao.save(proveedorEva);
    }


}
