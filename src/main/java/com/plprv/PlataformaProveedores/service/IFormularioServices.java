package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Formulario;

import java.util.List;

public interface IFormularioServices {

    public List<Formulario> encontrarFormularios(String forEstado, Integer idEmppal);

    public Formulario encontrarFormulariosPorId(Integer forId, String forEstado, Integer idEmppal);

    public Formulario encontrarFormulariosPorNombre(String forNombre, Integer idEmppal);

    public Integer cantidadFormularios(String forEstado, Integer idEmppal);

    public List<Formulario> encontrarFormulariosNombres(String forEstado, Integer idEmppal);

    public List<Formulario> encontrarFormulariosFiltro(String forEstado, String texto, Integer idEmppal);

    public void crearFormulario (Formulario forceso);

    public List<Formulario> encontrarFormulariosFiltroPaginas(String forEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasFormularios(String forEstado,  String texto, Integer idEmppal);

    public Formulario borrarFormulario(Formulario forceso);

    public Formulario actualizarFormulario(Formulario forceso);

}
