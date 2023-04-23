package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;

import java.util.List;

public interface IProveedorEvaServices {

    public List<ProveedorEva> encontrarProveedorEvaPorId(Integer preId);

    public ProveedorEva encontrarProveedorEvaPorPerId(Integer perId, Integer prvId);

    public List<ProveedorEva> encontrarProveedorEvaPorPrvId(Integer prvId);

    public void crearProveedorEva(ProveedorEva proveedorEva);

    public ProveedorEva actualizarProveedorEva(ProveedorEva proveedorEva);

    public List<ProveedorEva> encontrarProveedorEva(String preEstado);

    public ProveedorEva encontrarProveedorEvaPorId(Integer preId, String preEstado);

    public List<ProveedorEva> encontrarProveedorEvaPorFormulario(Integer perId, String preEstado);

    public Integer cantidadProveedorEva(String preEstado);

    public List<DocumentosProveedor> encontrarProveedoresEstados(Integer perId);
    public Long encontrarCantidadRequerida(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd);


    public List<ProveedorEva> encontrarProveedorEvaFiltro(String preEstado, String texto , Integer perId);


    public List<ProveedorEva> encontrarProveedorEvaFiltroPaginas(String preEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer perId);

    public Integer cantidadPaginasProveedorEva(String preEstado,  String texto, Integer perId);

    public ProveedorEva borrarProveedorEva(ProveedorEva preceso);

}
