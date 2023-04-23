package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Criticidad;

import java.util.List;

public interface ICriticidadServices {

    public List<Criticidad> encontrarCriticidads(String crtEstado);

    public Criticidad encontrarCriticidadsPorId(Integer crtId, String crtEstado);

    public Criticidad encontrarCriticidadsPorNombre(String crtNombre);

    public Integer cantidadCriticidads(String crtEstado);

    public List<Criticidad> encontrarCriticidadsNombres(String crtEstado);

    public List<Criticidad> encontrarCriticidadsFiltro(String crtEstado, String texto);

    public void crearCriticidad(Criticidad criticidad);

    public List<Criticidad> encontrarCriticidadsFiltroPaginas(String crtEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasCriticidads(String crtEstado, String texto);

    public Criticidad borrarCriticidad(Criticidad criticidad);

    public Criticidad actualizarCriticidad(Criticidad criticidad);

}
