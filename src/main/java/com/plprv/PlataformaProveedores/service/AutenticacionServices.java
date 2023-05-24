package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IAutenticacionDao;
import com.plprv.PlataformaProveedores.entity.Autenticacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutenticacionServices implements IAutenticacionServices {

    @Autowired
    private IAutenticacionDao autenticacionDao;
    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacion(String autEstado, Integer idEmppal) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoAndIdEmppalOrderByAutCorreoAsc(autEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public Autenticacion encontrarAutenticacionPorNombre(String autNombre, Integer idEmppal) {
        return (Autenticacion) autenticacionDao.findByAutCorreoAndIdEmppal(autNombre, idEmppal);
    }

    @Override
    public Autenticacion encontrarAutenticacionPorCodigo(String autCodigoCorreo) {
        return (Autenticacion) autenticacionDao.findByAutCodigoCorreo(autCodigoCorreo) ;
    }

    @Override
    @Transactional(readOnly = true)
    public Autenticacion encontrarAutenticacionPorId(Integer autId, String autEstado, Integer idEmppal) {
        if (autEstado == null) {
            return autenticacionDao.findByAutIdAndIdEmppal(autId, idEmppal);
        } else {
            return autenticacionDao.findByAutIdAndAutEstadoAndIdEmppal(autId, autEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadAutenticacion(String autEstado,Integer idEmppal) {
        List<Autenticacion> datos = (List<Autenticacion>) autenticacionDao.findByAutEstadoAndIdEmppalOrderByAutCorreoAsc(autEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        return datos.size();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacionNombres(String autEstado, Integer idEmppal) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoNombre(autEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autenticacion> encontrarAutenticacionFiltro(String autEstado, String texto, Integer idEmppal) {
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoFiltro(autEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Autenticacion> encontrarAutenticacionFiltroPaginas(String autEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "autId");
        return (List<Autenticacion>) autenticacionDao.findByAutEstadoPaginaFiltro(autEstado,texto.toLowerCase(), idEmppal, pageable);
    }

    @Override
    public void crearAutenticacion(Autenticacion criticidad) {
        autenticacionDao.save(criticidad);
    }
    @Override
    public Integer cantidadPaginasAutenticacion(String autEstado, String texto, Integer idEmppal) {
        List<Autenticacion> datos = (List<Autenticacion>) autenticacionDao.findByAutEstadoFiltro(autEstado,texto.toLowerCase(), idEmppal);
        return datos.size();
    }
    public Autenticacion borrarAutenticacion(Autenticacion criticidad) {
        return (Autenticacion) autenticacionDao.save(criticidad);
    }
    @Override
    public Autenticacion actualizarAutenticacion(Autenticacion criticidad) {
        return (Autenticacion) autenticacionDao.save(criticidad);

    }
}
