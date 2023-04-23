package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.ProveedorDoc;

import java.util.List;

public interface IProveedorDocServices {

    public ProveedorDoc encontrarProveedorDocsPorId(Integer prdId);

    public ProveedorDoc encontrarProveedorDocsPorFopId(Integer fopId, Integer prvId);

    public List<ProveedorDoc> encontrarProveedoresPorFopId(Integer fopId);

    public List<ProveedorDoc> encontrarProveedorDocsPorPrvId(Integer prvId);

    public void crearProveedorDoc(ProveedorDoc proveedorDoc);

    public ProveedorDoc actualizarProveedorDoc(ProveedorDoc proveedorDoc);

    public List<Object> encontrarTablaDocumental(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId , Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer numeroDePagina, Integer numeroElementosPorPagina);
    public List<Object> encontrarTablaEvaluacion(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado, Integer numeroDePagina, Integer numeroElementosPorPagina);
    public List<Object> encontrarTablaDocumentalTodo(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId , Integer proId, Integer sprId, String prdEstadoDocumental, String tipo);

    public Integer cantidadPaginasProveedorDoc(Integer prvId, Integer crtId,Integer forId, String texto, String prvNd, String prvNombre, Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo);
    public Integer cantidadPaginasProveedorEva(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado);


    public List<Object> descargarInformeGeneral(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado);

}
