package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Documentos;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;

import java.util.List;

public interface IProveedorDocServices {

    public ProveedorDoc encontrarProveedorDocsPorId(Integer prdId, Integer idEmppal);

    public ProveedorDoc encontrarProveedorDocsPorFopId(Integer fopId, Integer prvId, Integer idEmppal);

    public List<ProveedorDoc> encontrarProveedoresPorFopId(Integer fopId, Integer idEmppal);

    public List<ProveedorDoc> encontrarProveedorDocsPorPrvId(Integer prvId, Integer idEmppal);

    public void crearProveedorDoc(ProveedorDoc proveedorDoc);

    public ProveedorDoc actualizarProveedorDoc(ProveedorDoc proveedorDoc);

    public List<Object> encontrarTablaDocumental(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId , Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);
    public List<Object> encontrarTablaEvaluacion(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);
    public List<Documentos> encontrarTablaDocumentalTodo(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId , Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal);

    public Integer cantidadPaginasProveedorDoc(Integer prvId, Integer crtId,Integer forId, String texto, String prvNd, String prvNombre, Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal);
    public Integer cantidadPaginasProveedorEva(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado, Integer idEmppal);

    public void actualizarTodosEstado(Integer prvId, Integer crtId, Integer forId, String texto, String prvNd, String prvNombre, Integer perId , Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal);

    public List<Object> descargarInformeGeneral(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId, String preEstado, Integer idEmppal);

}
