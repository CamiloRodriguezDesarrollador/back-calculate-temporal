package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IFormularioDao;
import com.plprv.PlataformaProveedores.entity.Formulario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormularioServices implements IFormularioServices {

    @Autowired
    private IFormularioDao formularioDao;
    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormularios(String forEstado) {
        return (List<Formulario>) formularioDao.findByForEstado(forEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public Formulario encontrarFormulariosPorNombre(String forNombre) {
        return (Formulario) formularioDao.findByForNombre(forNombre);
    }
    @Override
    @Transactional(readOnly = true)
    public Formulario encontrarFormulariosPorId(Integer forId, String forEstado) {
        if (forEstado == null) {
            return formularioDao.findByForId(forId);
        } else {
            return formularioDao.findByForIdAndForEstado(forId, forEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadFormularios(String forEstado) {
        List<Formulario> datos = (List<Formulario>) formularioDao.findByForEstado(forEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormulariosNombres(String forEstado) {
        return (List<Formulario>) formularioDao.findByForEstadoNombre(forEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormulariosFiltro(String forEstado, String texto) {
        return (List<Formulario>) formularioDao.findByForEstadoFiltro(forEstado,texto.toLowerCase());
    }

    @Override
    public List<Formulario> encontrarFormulariosFiltroPaginas(String forEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Formulario>) formularioDao.findByForEstadoPaginaFiltro(forEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearFormulario(Formulario formulario) {
        formularioDao.save(formulario);
    }
    @Override
    public Integer cantidadPaginasFormularios(String forEstado, String texto) {
        List<Formulario> datos = (List<Formulario>) formularioDao.findByForEstadoFiltro(forEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Formulario borrarFormulario(Formulario formulario) {
        return (Formulario) formularioDao.save(formulario);
    }
    @Override
    public Formulario actualizarFormulario(Formulario formulario) {
        return (Formulario) formularioDao.save(formulario);

    }
}
