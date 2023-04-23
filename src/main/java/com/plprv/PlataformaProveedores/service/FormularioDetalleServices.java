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
    public List<FormularioDetalle> encontrarFormularioDetalles(String fodEstado) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoOrderByFodNombre(fodEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public FormularioDetalle encontrarFormularioDetallesPorNombre(String fodNombre, Integer forId) {
        return (FormularioDetalle) formularioDetalleDao.findByFodNombreAndForId(fodNombre,forId);
    }
    @Override
    @Transactional(readOnly = true)
    public FormularioDetalle encontrarFormularioDetallesPorId(Integer fodId, String fodEstado) {
        if (fodEstado == null) {
            return formularioDetalleDao.findByFodId(fodId);
        } else {
            return formularioDetalleDao.findByFodIdAndFodEstado(fodId, fodEstado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesPorFormulario(Integer forId, String fodEstado) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByForIdAndFodEstado(forId, fodEstado);
    }
    @Transactional(readOnly = true)
    public Integer cantidadFormularioDetalles(String fodEstado) {
        List<FormularioDetalle> datos = (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoOrderByFodNombre(fodEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesNombres(String fodEstado) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoNombre(fodEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDetalle> encontrarFormularioDetallesFiltro(String fodEstado, String texto, Integer forId) {
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoFiltro(fodEstado,texto.toLowerCase(),forId);
    }

    @Override
    public List<FormularioDetalle> encontrarFormularioDetallesFiltroPaginas(String fodEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer forId) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoPaginaFiltro(fodEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial,forId);
    }

    @Override
    public void crearFormularioDetalle(FormularioDetalle formularioDetalle) {
        formularioDetalleDao.save(formularioDetalle);
    }
    @Override
    public Integer cantidadPaginasFormularioDetalles(String fodEstado, String texto, Integer forId) {
        List<FormularioDetalle> datos = (List<FormularioDetalle>) formularioDetalleDao.findByFodEstadoFiltro(fodEstado,texto.toLowerCase(), forId);
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
