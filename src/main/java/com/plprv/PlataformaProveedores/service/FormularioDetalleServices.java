package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IFormularioDetalleDao;
import com.plprv.PlataformaProveedores.entity.FormularioDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormularioDetalleServices implements IFormularioDetalleServices {

    @Autowired
    private IFormularioDetalleDao formularioDetalleDao;
    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetalles(String fodEstado, Integer idEmppal) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoAndIdEmppalOrderByFodNombre(fodEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public FormularioDetalle encontrarFormularioDetallesPorNombre(String fodNombre, Integer forId, Integer idEmppal) {
        return (FormularioDetalle) formularioDetalleDao.findByFodNombreAndForIdAndIdEmppal(fodNombre,forId, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public FormularioDetalle encontrarFormularioDetallesPorId(Integer fodId, String fodEstado, Integer idEmppal) {
        if (fodEstado == null) {
            return formularioDetalleDao.findByFodIdAndIdEmppal(fodId, idEmppal);
        } else {
            return formularioDetalleDao.findByFodIdAndFodEstadoAndIdEmppal(fodId, fodEstado, idEmppal);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesPorFormulario(Integer forId, String fodEstado,Integer idEmppal) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByForIdAndFodEstadoAndIdEmppal(forId, fodEstado, idEmppal);
    }
    @Transactional(readOnly = true)
    public Integer cantidadFormularioDetalles(String fodEstado, Integer idEmppal) {
        List<FormularioDetalle> datos = (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoAndIdEmppalOrderByFodNombre(fodEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesNombres(String fodEstado, Integer idEmppal) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoNombre(fodEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesFiltro(String fodEstado, String texto, Integer forId, Integer idEmppal) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoFiltro(fodEstado,texto.toLowerCase(),forId, idEmppal);
    }

    @Override
    public List<FormularioDetalle> encontrarFormularioDetallesFiltroPaginas(String fodEstado, String texto, Integer numeroDePagina,
                                                                            Integer numeroElementosPorPagina, Integer forId, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoPaginaFiltro(fodEstado,texto.toLowerCase(),numeroElementosPorPagina,
                limiteInicial,forId, idEmppal);
    }

    @Override
    public void crearFormularioDetalle(FormularioDetalle formularioDetalle) {
        formularioDetalleDao.save(formularioDetalle);
    }
    @Override
    public Integer cantidadPaginasFormularioDetalles(String fodEstado, String texto, Integer forId, Integer idEmppal) {
        List<FormularioDetalle> datos = (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoFiltro(fodEstado,texto.toLowerCase(), forId, idEmppal);
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public FormularioDetalle borrarFormularioDetalle(FormularioDetalle formularioDetalle) {
        return (FormularioDetalle) formularioDetalleDao.save(formularioDetalle);
    }
    @Override
    public FormularioDetalle actualizarFormularioDetalle(FormularioDetalle formularioDetalle) {
        return (FormularioDetalle) formularioDetalleDao.save(formularioDetalle);

    }
}
