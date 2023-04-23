package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Autenticacion;

import java.util.List;

public interface IAutenticationprvServices {

    public List<Autenticacion> encontrarAutenticationprv(String autEstado);

    public List<Autenticacion> encontrarAutenticationprvPorId(Integer autId);

    public void verificarCorreo(Autenticacion autenticacion);

    public void verificarCodigo(Integer autCodigoCorreo);

    public Integer cantidadAutenticationprv();

    public List<Autenticacion> encontrarAutenticationprvNombres(String autEstado);

    public List<Autenticacion> encontrarAutenticationprvFiltro(String autEstado, String texto);

    public void crearAutenticationprv (Autenticacion autenticacion);

    public List<Autenticacion> encontrarAutenticationprvFiltroPaginas(String autEstado, String texto, Integer numeroDePagina);

    public Integer cantidadPaginasAutenticationprv(Integer numeroElementosPorPagina , String texto);

    public void borrarAutenticationprv(Integer autId);

    public Autenticacion actualizarAutenticationprv(Autenticacion autenticacion);

}
