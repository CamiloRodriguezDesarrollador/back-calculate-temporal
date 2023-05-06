package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Autenticacion;

import java.util.List;

public interface IAutenticacionServices {

    public List<Autenticacion> encontrarAutenticacion(String autEstado, Integer idEmppal);

    public Autenticacion encontrarAutenticacionPorId(Integer autId, String autEstado, Integer idEmppal);

    public Autenticacion encontrarAutenticacionPorNombre(String autNombre, Integer idEmppal);

    public Autenticacion encontrarAutenticacionPorCodigo(String autCodigoCorreo);

    public Integer cantidadAutenticacion(String autEstado, Integer idEmppal);

    public List<Autenticacion> encontrarAutenticacionNombres(String autEstado, Integer idEmppal);

    public List<Autenticacion> encontrarAutenticacionFiltro(String autEstado, String texto, Integer idEmppal);

    public void crearAutenticacion(Autenticacion criticidad);

    public List<Autenticacion> encontrarAutenticacionFiltroPaginas(String autEstado, String texto, Integer numeroDePagina,
                                                                   Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasAutenticacion(String autEstado, String texto, Integer idEmppal);

    public Autenticacion borrarAutenticacion(Autenticacion criticidad);

    public Autenticacion actualizarAutenticacion(Autenticacion criticidad);

}
