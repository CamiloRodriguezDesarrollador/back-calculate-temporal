package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Proceso;

import java.util.List;

public interface IProcesoServices {

    public List<Proceso> encontrarProcesos(String proEstado, Integer idEmppal);

    public Proceso encontrarProcesosPorId(Integer proId, String proEstado, Integer idEmppal);

    public Proceso encontrarProcesosPorNombre(String proNombre, Integer idEmppal);

    public Integer cantidadProcesos(String proEstado, Integer idEmppal);

    public List<Proceso> encontrarProcesosNombres(String proEstado, Integer idEmppal);

    public List<Proceso> encontrarProcesosFiltro(String proEstado, String texto, Integer idEmppal);

    public void crearProceso (Proceso proceso);

    public List<Proceso> encontrarProcesosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasProcesos(String proEstado,  String texto, Integer idEmppal);

    public Proceso borrarProceso(Proceso proceso);

    public Proceso actualizarProceso(Proceso proceso);

}
