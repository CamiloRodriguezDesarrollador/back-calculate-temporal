package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Formulario;

import java.util.List;

public interface IFormularioServices {

    public List<Formulario> encontrarFormularios(String forEstado);

    public Formulario encontrarFormulariosPorId(Integer forId, String forEstado);

    public Formulario encontrarFormulariosPorNombre(String forNombre);

    public Integer cantidadFormularios(String forEstado);

    public List<Formulario> encontrarFormulariosNombres(String forEstado);

    public List<Formulario> encontrarFormulariosFiltro(String forEstado, String texto);

    public void crearFormulario (Formulario forceso);

    public List<Formulario> encontrarFormulariosFiltroPaginas(String forEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasFormularios(String forEstado,  String texto);

    public Formulario borrarFormulario(Formulario forceso);

    public Formulario actualizarFormulario(Formulario forceso);

}
