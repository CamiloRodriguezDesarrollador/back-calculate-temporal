package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.SubProceso;

import java.util.List;

public interface ISubProcesoServices {

    public List<SubProceso> encontrarSubProcesos(String sprEstado, Integer idEmppal);

    public SubProceso encontrarSubProcesosPorId(Integer sprId, String sprEstado, Integer idEmppal);

    public SubProceso encontrarSubProcesosPorNombre(String sprNombre, Integer proId, Integer idEmppal);

    public Integer cantidadSubProcesos(String sprEstado, Integer idEmppal);

    public List<SubProceso> encontrarSubProcesosNombres(String sprEstado, Integer idEmppal);

    public List<SubProceso> encontrarSubProcesosFiltro(String sprEstado, String texto , Integer proId, Integer idEmppal);

    public void crearSubProceso (SubProceso subProceso);

    public List<SubProceso> encontrarSubProcesosFiltroPaginas(String sprEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer proId, Integer idEmppal);

    public Integer cantidadPaginasSubProcesos(String sprEstado,  String texto , Integer proId, Integer idEmppal);

    public SubProceso borrarSubProceso(SubProceso sprceso);

    public SubProceso actualizarSubProceso(SubProceso subProceso);

}
