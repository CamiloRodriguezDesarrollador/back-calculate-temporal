package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;

import java.util.List;

public interface IProveedorEvaServices {

    public List<ProveedorEva> encontrarProveedorEvaPorId(Integer preId, Integer idEmppal);

    public ProveedorEva encontrarProveedorEvaPorPerId(Integer perId, Integer prvId, Integer idEmppal);

    public List<ProveedorEva> encontrarProveedorEvaPorPrvId(Integer prvId, Integer idEmppal);

    public void crearProveedorEva(ProveedorEva proveedorEva);

    public ProveedorEva actualizarProveedorEva(ProveedorEva proveedorEva);

    public List<ProveedorEva> encontrarProveedorEva(String preEstado, Integer idEmppal);

    public ProveedorEva encontrarProveedorEvaPorId(Integer preId, String preEstado, Integer idEmppal);

    public List<ProveedorEva> encontrarProveedorEvaPorFormulario(Integer perId, String preEstado, Integer idEmppal);

    public Integer cantidadProveedorEva(String preEstado, Integer idEmppal);

    public List<DocumentosProveedor> encontrarProveedoresEstados(Integer perId, Integer idEmppal);
    public Long encontrarCantidadRequerida(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd, Integer idEmppal);


    public List<ProveedorEva> encontrarProveedorEvaFiltro(String preEstado, String texto , Integer perId, Integer idEmppal);


    public List<ProveedorEva> encontrarProveedorEvaFiltroPaginas(String preEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer perId, Integer idEmppal);

    public Integer cantidadPaginasProveedorEva(String preEstado,  String texto, Integer perId, Integer idEmppal);

    public ProveedorEva borrarProveedorEva(ProveedorEva preceso);

}
