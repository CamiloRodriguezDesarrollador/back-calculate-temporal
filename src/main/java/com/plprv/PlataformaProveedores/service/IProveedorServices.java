package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Proveedor;

import java.util.List;

public interface IProveedorServices {

    public List<Proveedor> encontrarProveedores(String prvEstado);

    public Proveedor encontrarProveedoresPorId(Integer prvId, String prvEstado);

    public Proveedor encontrarProveedoresPorNdyTdcTd(String prvNd, String tdcTd);

    public Proveedor encontrarProveedoresSoloPorId(Integer prvId);

    public Proveedor encontrarProveedoresPorToken(String prvToken, String prvEstado);
    public Proveedor encontrarProveedoresPorNombre(String prvNombre);

    public List<Proveedor> encontrarProveedoresPorCriticidad(String prvEstado , Integer crtId);

    public Integer cantidadProveedores(String prvEstado);

    public List<Proveedor> encontrarProveedoresNombres(String prvEstado, String texto);

    public List<Proveedor> encontrarProveedoresFiltro(String prvEstado, String texto);

    public void crearProveedor (Proveedor prvveedor);

    public List<Proveedor> encontrarProveedoresFiltroPaginas(String prvEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasProveedores(String prvEstado,  String texto);

    public Proveedor borrarProveedor(Proveedor prvveedor);

    public Proveedor actualizarProveedor(Proveedor prvveedor);

}
