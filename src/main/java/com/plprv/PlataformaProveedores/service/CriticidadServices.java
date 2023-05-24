package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.ICriticidadDao;
import com.plprv.PlataformaProveedores.entity.Criticidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CriticidadServices implements ICriticidadServices {

    @Autowired
    private ICriticidadDao criticidadDao;
    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidads(String crtEstado, Integer idEmppal) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoAndIdEmppalOrderByCrtNombreAsc(crtEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public Criticidad encontrarCriticidadsPorNombre(String crtNombre, Integer idEmppal) {
        return (Criticidad) criticidadDao.findByCrtNombreAndIdEmppal(crtNombre, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Criticidad encontrarCriticidadsPorId(Integer crtId, String crtEstado, Integer idEmppal) {
        if (crtEstado == null) {
            return criticidadDao.findByCrtIdAndIdEmppal(crtId, idEmppal);
        } else {
            return criticidadDao.findByCrtIdAndCrtEstadoAndIdEmppal(crtId, crtEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadCriticidads(String crtEstado, Integer idEmppal) {
        List<Criticidad> datos = (List<Criticidad>) criticidadDao.findByCrtEstadoAndIdEmppalOrderByCrtNombreAsc(crtEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidadsNombres(String crtEstado, Integer idEmppal) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoNombre(crtEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidadsFiltro(String crtEstado, String texto, Integer idEmppal) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoFiltro(crtEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Criticidad> encontrarCriticidadsFiltroPaginas(String crtEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "crtId");
        return (List<Criticidad>) criticidadDao.findByCrtEstadoPaginaFiltro(crtEstado,texto.toLowerCase(), idEmppal, pageable);
    }

    @Override
    public void crearCriticidad(Criticidad criticidad) {
        criticidadDao.save(criticidad);
    }
    @Override
    public Integer cantidadPaginasCriticidads(String crtEstado, String texto, Integer idEmppal) {
        List<Criticidad> datos = (List<Criticidad>) criticidadDao.findByCrtEstadoFiltro(crtEstado,texto.toLowerCase(), idEmppal);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Criticidad borrarCriticidad(Criticidad criticidad) {
        return (Criticidad) criticidadDao.save(criticidad);
    }
    @Override
    public Criticidad actualizarCriticidad(Criticidad criticidad) {
        return (Criticidad) criticidadDao.save(criticidad);

    }
}
