package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.FormularioDetalle;

import java.util.List;

public interface IFormularioDetalleServices {

    public List<FormularioDetalle> encontrarFormularioDetalles(String fodEstado, Integer idEmppal);

    public FormularioDetalle encontrarFormularioDetallesPorId(Integer fodId, String fodEstado, Integer idEmppal);

    public List<FormularioDetalle> encontrarFormularioDetallesPorFormulario(Integer forId, String fodEstado, Integer idEmppal);

    public FormularioDetalle encontrarFormularioDetallesPorNombre(String fodNombre,Integer forId, Integer idEmppal);

    public Integer cantidadFormularioDetalles(String fodEstado, Integer idEmppal);

    public List<FormularioDetalle> encontrarFormularioDetallesNombres(String fodEstado, Integer idEmppal);

    public List<FormularioDetalle> encontrarFormularioDetallesFiltro(String fodEstado, String texto , Integer forId, Integer idEmppal);

    public void crearFormularioDetalle (FormularioDetalle fodceso);

    public List<FormularioDetalle> encontrarFormularioDetallesFiltroPaginas(String fodEstado, String texto, Integer numeroDePagina,
                                                                            Integer numeroElementosPorPagina, Integer forId, Integer idEmppal);

    public Integer cantidadPaginasFormularioDetalles(String fodEstado,  String texto, Integer forId, Integer idEmppal);

    public FormularioDetalle borrarFormularioDetalle(FormularioDetalle fodceso);

    public FormularioDetalle actualizarFormularioDetalle(FormularioDetalle fodceso);

}
