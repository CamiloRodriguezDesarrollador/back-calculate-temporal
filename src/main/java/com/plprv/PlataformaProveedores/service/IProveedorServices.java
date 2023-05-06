package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorSistema;
import com.plprv.PlataformaProveedores.entity.ProveedorSistemaDetalle;

import java.util.List;

public interface IProveedorServices {

    public List<Proveedor> encontrarProveedores(String prvEstado, Integer idEmppal);

    public Proveedor encontrarProveedoresPorId(Integer prvId, String prvEstado, Integer idEmppal);

    public Proveedor encontrarProveedorPorCorreo(String prvCorreo, Integer idEmppal);
    public Proveedor encontrarProveedorPorToken(String prvToken, Integer idEmppal);

    public Proveedor encontrarProveedoresPorNdyTdcTd(String prvNd, String tdcTd, Integer idEmppal);

    public Proveedor encontrarProveedoresSoloPorId(Integer prvId, Integer idEmppal);

    public Proveedor encontrarProveedoresPorToken(String prvToken, String prvEstado, Integer idEmppal);
    public Proveedor encontrarProveedoresPorNombre(String prvNombre, Integer idEmppal);

    public List<Proveedor> encontrarProveedoresPorCriticidad(String prvEstado , Integer crtId, Integer idEmppal);

    public Integer cantidadProveedores(String prvEstado, Integer idEmppal);

    public List<Proveedor> encontrarProveedoresNombres(String prvEstado, String texto, Integer idEmppal);

    public Object encontrarProveedorSistema(Integer nitNd, String tdcTd);

//    public ProveedorSistemaDetalle encontrarProveedorSistemaDetalle(Integer nitNd, String tdcTd);


    public List<Proveedor> encontrarProveedoresFiltro(String prvEstado, String texto, Integer idEmppal);

    public void crearProveedor (Proveedor prvveedor);

    public List<Proveedor> encontrarProveedoresFiltroPaginas(String prvEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasProveedores(String prvEstado,  String texto, Integer idEmppal);

    public Proveedor borrarProveedor(Proveedor prvveedor);

    public Proveedor actualizarProveedor(Proveedor prvveedor);

}
