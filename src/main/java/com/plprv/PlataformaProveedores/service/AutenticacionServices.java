package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IAutenticacionDao;
import com.plprv.PlataformaProveedores.entity.Autenticacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutenticacionServices implements IAutenticacionServices {

    @Autowired
    private IAutenticacionDao autenticacionDao;
    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacion(String autEstado) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoOrderByAutCorreoAsc(autEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Autenticacion encontrarAutenticacionPorNombre(String autNombre) {
        return (Autenticacion) autenticacionDao.findByAutCorreo(autNombre);
    }

    @Override
    public Autenticacion encontrarAutenticacionPorCodigo(String autCodigoCorreo) {
        return (Autenticacion) autenticacionDao.findByAutCodigoCorreo(autCodigoCorreo) ;
    }

    @Override
    @Transactional(readOnly = true)
    public Autenticacion encontrarAutenticacionPorId(Integer autId, String autEstado) {
        if (autEstado == null) {
            return autenticacionDao.findByAutId(autId);
        } else {
            return autenticacionDao.findByAutIdAndAutEstado(autId, autEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadAutenticacion(String autEstado) {
        List<Autenticacion> datos = (List<Autenticacion>) autenticacionDao.findByAutEstadoOrderByAutCorreoAsc(autEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacionNombres(String autEstado) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoNombre(autEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacionFiltro(String autEstado, String texto) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoFiltro(autEstado,texto.toLowerCase());
    }

    @Override
    public List<Autenticacion> encontrarAutenticacionFiltroPaginas(String autEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoPaginaFiltro(autEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearAutenticacion(Autenticacion criticidad) {
        autenticacionDao.save(criticidad);
    }
    @Override
    public Integer cantidadPaginasAutenticacion(String autEstado, String texto) {
        List<Autenticacion> datos = (List<Autenticacion>) autenticacionDao.findByAutEstadoFiltro(autEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Autenticacion borrarAutenticacion(Autenticacion criticidad) {
        return (Autenticacion) autenticacionDao.save(criticidad);
    }
    @Override
    public Autenticacion actualizarAutenticacion(Autenticacion criticidad) {
        return (Autenticacion) autenticacionDao.save(criticidad);

    }
}
