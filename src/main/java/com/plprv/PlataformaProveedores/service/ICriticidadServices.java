package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Criticidad;

import java.util.List;

public interface ICriticidadServices {

    public List<Criticidad> encontrarCriticidads(String crtEstado, Integer idEmppal);

    public Criticidad encontrarCriticidadsPorId(Integer crtId, String crtEstado, Integer idEmppal);

    public Criticidad encontrarCriticidadsPorNombre(String crtNombre, Integer idEmppal);

    public Integer cantidadCriticidads(String crtEstado, Integer idEmppal);

    public List<Criticidad> encontrarCriticidadsNombres(String crtEstado, Integer idEmppal);

    public List<Criticidad> encontrarCriticidadsFiltro(String crtEstado, String texto, Integer idEmppal);

    public void crearCriticidad(Criticidad criticidad);

    public List<Criticidad> encontrarCriticidadsFiltroPaginas(String crtEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasCriticidads(String crtEstado, String texto, Integer idEmppal);

    public Criticidad borrarCriticidad(Criticidad criticidad);

    public Criticidad actualizarCriticidad(Criticidad criticidad);

}
