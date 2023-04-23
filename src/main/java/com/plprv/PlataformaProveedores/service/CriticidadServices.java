package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.ICriticidadDao;
import com.plprv.PlataformaProveedores.entity.Criticidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CriticidadServices implements ICriticidadServices {

    @Autowired
    private ICriticidadDao criticidadDao;
    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidads(String crtEstado) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoOrderByCrtNombreAsc(crtEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Criticidad encontrarCriticidadsPorNombre(String crtNombre) {
        return (Criticidad) criticidadDao.findByCrtNombre(crtNombre);
    }
    @Override
    @Transactional(readOnly = true)
    public Criticidad encontrarCriticidadsPorId(Integer crtId, String crtEstado) {
        if (crtEstado == null) {
            return criticidadDao.findByCrtId(crtId);
        } else {
            return criticidadDao.findByCrtIdAndCrtEstado(crtId, crtEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadCriticidads(String crtEstado) {
        List<Criticidad> datos = (List<Criticidad>) criticidadDao.findByCrtEstadoOrderByCrtNombreAsc(crtEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidadsNombres(String crtEstado) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoNombre(crtEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Criticidad> encontrarCriticidadsFiltro(String crtEstado, String texto) {
        return (List<Criticidad>) criticidadDao.findByCrtEstadoFiltro(crtEstado,texto.toLowerCase());
    }

    @Override
    public List<Criticidad> encontrarCriticidadsFiltroPaginas(String crtEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Criticidad>) criticidadDao.findByCrtEstadoPaginaFiltro(crtEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearCriticidad(Criticidad criticidad) {
        criticidadDao.save(criticidad);
    }
    @Override
    public Integer cantidadPaginasCriticidads(String crtEstado, String texto) {
        List<Criticidad> datos = (List<Criticidad>) criticidadDao.findByCrtEstadoFiltro(crtEstado,texto.toLowerCase());
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
