package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Proceso;

import java.util.List;

public interface IProcesoServices {

    public List<Proceso> encontrarProcesos(String proEstado);

    public Proceso encontrarProcesosPorId(Integer proId, String proEstado);

    public Proceso encontrarProcesosPorNombre(String proNombre);

    public Integer cantidadProcesos(String proEstado);

    public List<Proceso> encontrarProcesosNombres(String proEstado);

    public List<Proceso> encontrarProcesosFiltro(String proEstado, String texto);

    public void crearProceso (Proceso proceso);

    public List<Proceso> encontrarProcesosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasProcesos(String proEstado,  String texto);

    public Proceso borrarProceso(Proceso proceso);

    public Proceso actualizarProceso(Proceso proceso);

}
