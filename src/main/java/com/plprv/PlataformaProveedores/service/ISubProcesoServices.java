package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.SubProceso;

import java.util.List;

public interface ISubProcesoServices {

    public List<SubProceso> encontrarSubProcesos(String sprEstado);

    public SubProceso encontrarSubProcesosPorId(Integer sprId, String sprEstado);

    public SubProceso encontrarSubProcesosPorNombre(String sprNombre, Integer proId);

    public Integer cantidadSubProcesos(String sprEstado);

    public List<SubProceso> encontrarSubProcesosNombres(String sprEstado);

    public List<SubProceso> encontrarSubProcesosFiltro(String sprEstado, String texto , Integer proId);

    public void crearSubProceso (SubProceso subProceso);

    public List<SubProceso> encontrarSubProcesosFiltroPaginas(String sprEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer proId);

    public Integer cantidadPaginasSubProcesos(String sprEstado,  String texto , Integer proId);

    public SubProceso borrarSubProceso(SubProceso sprceso);

    public SubProceso actualizarSubProceso(SubProceso subProceso);

}
