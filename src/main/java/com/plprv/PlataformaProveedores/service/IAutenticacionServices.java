package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Autenticacion;

import java.util.List;

public interface IAutenticacionServices {

    public List<Autenticacion> encontrarAutenticacion(String autEstado);

    public Autenticacion encontrarAutenticacionPorId(Integer autId, String autEstado);

    public Autenticacion encontrarAutenticacionPorNombre(String autNombre);

    public Autenticacion encontrarAutenticacionPorCodigo(String autCodigoCorreo);

    public Integer cantidadAutenticacion(String autEstado);

    public List<Autenticacion> encontrarAutenticacionNombres(String autEstado);

    public List<Autenticacion> encontrarAutenticacionFiltro(String autEstado, String texto);

    public void crearAutenticacion(Autenticacion criticidad);

    public List<Autenticacion> encontrarAutenticacionFiltroPaginas(String autEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasAutenticacion(String autEstado, String texto);

    public Autenticacion borrarAutenticacion(Autenticacion criticidad);

    public Autenticacion actualizarAutenticacion(Autenticacion criticidad);

}
