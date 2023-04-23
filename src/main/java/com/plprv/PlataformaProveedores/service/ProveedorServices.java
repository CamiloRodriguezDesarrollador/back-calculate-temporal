package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IProveedorDao;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorServices implements IProveedorServices {

    @Autowired
    private IProveedorDao proveedorDao;
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedores(String prvEstado) {
        return (List<Proveedor>) proveedorDao.findByPrvEstado(prvEstado);
    }
    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorNombre(String prvNombre) {
        return (Proveedor) proveedorDao.findByPrvNombre(prvNombre);
    }

    @Override
    public List<Proveedor> encontrarProveedoresPorCriticidad(String prvEstado ,Integer crtId) {
        return proveedorDao.findByPrvEstadoAndCrtId(prvEstado,crtId);
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorId(Integer prvId, String prvEstado) {
        return (Proveedor) proveedorDao.findByPrvIdAndPrvEstado(prvId, prvEstado);
    }

    @Override
    public Proveedor encontrarProveedoresPorNdyTdcTd(String prvNd, String tdcTd) {
        return (Proveedor) proveedorDao.findByPrvNdAndTdcTd(prvNd,tdcTd) ;
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresSoloPorId(Integer prvId) {
        return (Proveedor) proveedorDao.findByPrvId(prvId);
    }
    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorToken(String prvToken, String prvEstado) {
        return (Proveedor) proveedorDao.findByPrvTokenAndPrvEstado(prvToken, prvEstado);
    }
    @Transactional(readOnly = true)
    public Integer cantidadProveedores(String prvEstado) {
        List<Proveedor> datos = (List<Proveedor>) proveedorDao.findByPrvEstado(prvEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedoresNombres(String prvEstado, String texto) {
        return (List<Proveedor>) proveedorDao.findByPrvEstadoNombre(prvEstado,texto.toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedoresFiltro(String prvEstado, String texto) {
        return (List<Proveedor>) proveedorDao.findByPrvEstadoFiltro(prvEstado,texto.toLowerCase());
    }

    @Override
    public List<Proveedor> encontrarProveedoresFiltroPaginas(String prvEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Proveedor>) proveedorDao.findByPrvEstadoPaginaFiltro(prvEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearProveedor(Proveedor proveedor) {
        proveedorDao.save(proveedor);
    }
    @Override
    public Integer cantidadPaginasProveedores(String prvEstado, String texto) {
        List<Proveedor> datos = (List<Proveedor>) proveedorDao.findByPrvEstadoFiltro(prvEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Proveedor borrarProveedor(Proveedor proveedor) {
        return (Proveedor) proveedorDao.save(proveedor);
    }
    @Override
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        return (Proveedor) proveedorDao.save(proveedor);

    }
}
