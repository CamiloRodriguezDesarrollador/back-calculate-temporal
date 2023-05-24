package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IFormularioDao;
import com.plprv.PlataformaProveedores.entity.Formulario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormularioServices implements IFormularioServices {

    @Autowired
    private IFormularioDao formularioDao;
    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormularios(String forEstado, Integer idEmppal) {
        return (List<Formulario>) formularioDao.findByForEstadoAndIdEmppalOrderByForNombreAsc(forEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public Formulario encontrarFormulariosPorNombre(String forNombre, Integer idEmppal) {
        return (Formulario) formularioDao.findByForNombreAndIdEmppal(forNombre, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Formulario encontrarFormulariosPorId(Integer forId, String forEstado, Integer idEmppal) {
        if (forEstado == null) {
            return formularioDao.findByForIdAndIdEmppal(forId, idEmppal);
        } else {
            return formularioDao.findByForIdAndForEstadoAndIdEmppal(forId, forEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadFormularios(String forEstado, Integer idEmppal) {
        List<Formulario> datos = (List<Formulario>) formularioDao.findByForEstadoAndIdEmppalOrderByForNombreAsc(forEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormulariosNombres(String forEstado, Integer idEmppal) {
        return (List<Formulario>) formularioDao.findByForEstadoNombre(forEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Formulario> encontrarFormulariosFiltro(String forEstado, String texto, Integer idEmppal) {
        return (List<Formulario>) formularioDao.findByForEstadoFiltro(forEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Formulario> encontrarFormulariosFiltroPaginas(String forEstado, String texto, Integer numeroDePagina,
                                                              Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "forId");
        return (List<Formulario>) formularioDao.findByForEstadoPaginaFiltro(forEstado,texto.toLowerCase(), idEmppal, pageable);
    }

    @Override
    public void crearFormulario(Formulario formulario) {
        formularioDao.save(formulario);
    }
    @Override
    public Integer cantidadPaginasFormularios(String forEstado, String texto, Integer idEmppal) {
        List<Formulario> datos = (List<Formulario>) formularioDao.findByForEstadoFiltro(forEstado,texto.toLowerCase(), idEmppal);
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
