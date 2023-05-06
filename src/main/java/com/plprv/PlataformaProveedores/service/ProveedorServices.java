package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IClienteDao;
import com.plprv.PlataformaProveedores.dao.IProveedorDao;
import com.plprv.PlataformaProveedores.dao.IProveedorSistemaDao;
import com.plprv.PlataformaProveedores.dao.IProveedorSistemaDetalleDao;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorSistema;
import com.plprv.PlataformaProveedores.entity.ProveedorSistemaDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorServices implements IProveedorServices {

    @Autowired
    private IProveedorDao proveedorDao;

    @Autowired
    private IProveedorSistemaDao proveedorSistemaDao;

    @Autowired
    private IProveedorSistemaDetalleDao proveedorSistemaDetalleDao;
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedores(String prvEstado, Integer idEmppal) {
        return (List<Proveedor>) proveedorDao.findByPrvEstadoAndIdEmppal(prvEstado, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorNombre(String prvNombre, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvNombreAndIdEmppal(prvNombre, idEmppal);
    }


    @Override
    public List<Proveedor> encontrarProveedoresPorCriticidad(String prvEstado ,Integer crtId, Integer idEmppal) {
        return proveedorDao.findByPrvEstadoAndCrtIdAndIdEmppal(prvEstado,crtId, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorId(Integer prvId, String prvEstado, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvIdAndPrvEstadoAndIdEmppal(prvId, prvEstado, idEmppal);
    }

    @Override
    public Proveedor encontrarProveedorPorCorreo(String prvCorreo, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvCorreoAndIdEmppal(prvCorreo, idEmppal);
    }

    @Override
    public Proveedor encontrarProveedorPorToken(String prvToken, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvTokenAndIdEmppal(prvToken, idEmppal);
    }

    @Override
    public Proveedor encontrarProveedoresPorNdyTdcTd(String prvNd, String tdcTd, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvNdAndTdcTdAndIdEmppal(prvNd,tdcTd, idEmppal) ;
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresSoloPorId(Integer prvId, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvIdAndIdEmppal(prvId, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Proveedor encontrarProveedoresPorToken(String prvToken, String prvEstado, Integer idEmppal) {
        return (Proveedor) proveedorDao.findByPrvTokenAndPrvEstadoAndIdEmppal(prvToken, prvEstado, idEmppal);
    }
    @Transactional(readOnly = true)
    public Integer cantidadProveedores(String prvEstado, Integer idEmppal) {
        List<Proveedor> datos = (List<Proveedor>) proveedorDao.findByPrvEstadoAndIdEmppal(prvEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedoresNombres(String prvEstado, String texto, Integer idEmppal) {
        return (List<Proveedor>) proveedorDao.findByPrvEstadoNombre(prvEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public Object encontrarProveedorSistema(Integer nitNd, String tdcTd) {
        return  (Object) proveedorSistemaDao.cantidadDocumentacionTablaDetalle(nitNd,tdcTd) ;
    }

//    @Override
//    public ProveedorSistemaDetalle encontrarProveedorSistemaDetalle(Integer nitNd, String tdcTd) {
//        return  (ProveedorSistemaDetalle) proveedorSistemaDetalleDao.findByNitNdAndTdcTd(nitNd,tdcTd) ;
//    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> encontrarProveedoresFiltro(String prvEstado, String texto, Integer idEmppal) {
        return (List<Proveedor>) proveedorDao.findByPrvEstadoFiltro(prvEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Proveedor> encontrarProveedoresFiltroPaginas(String prvEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Proveedor>) proveedorDao.findByPrvEstadoPaginaFiltro(prvEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial, idEmppal);
    }

    @Override
    public void crearProveedor(Proveedor proveedor) {
        proveedorDao.save(proveedor);
    }
    @Override
    public Integer cantidadPaginasProveedores(String prvEstado, String texto, Integer idEmppal) {
        List<Proveedor> datos = (List<Proveedor>) proveedorDao.findByPrvEstadoFiltro(prvEstado,texto.toLowerCase(), idEmppal);
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
