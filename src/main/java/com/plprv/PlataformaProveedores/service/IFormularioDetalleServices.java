package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.FormularioDetalle;

import java.util.List;

public interface IFormularioDetalleServices {

    public List<FormularioDetalle> encontrarFormularioDetalles(String fodEstado);

    public FormularioDetalle encontrarFormularioDetallesPorId(Integer fodId, String fodEstado);

    public List<FormularioDetalle> encontrarFormularioDetallesPorFormulario(Integer forId, String fodEstado);

    public FormularioDetalle encontrarFormularioDetallesPorNombre(String fodNombre,Integer forId);

    public Integer cantidadFormularioDetalles(String fodEstado);

    public List<FormularioDetalle> encontrarFormularioDetallesNombres(String fodEstado);

    public List<FormularioDetalle> encontrarFormularioDetallesFiltro(String fodEstado, String texto , Integer forId);

    public void crearFormularioDetalle (FormularioDetalle fodceso);

    public List<FormularioDetalle> encontrarFormularioDetallesFiltroPaginas(String fodEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer forId);

    public Integer cantidadPaginasFormularioDetalles(String fodEstado,  String texto, Integer forId);

    public FormularioDetalle borrarFormularioDetalle(FormularioDetalle fodceso);

    public FormularioDetalle actualizarFormularioDetalle(FormularioDetalle fodceso);

}
