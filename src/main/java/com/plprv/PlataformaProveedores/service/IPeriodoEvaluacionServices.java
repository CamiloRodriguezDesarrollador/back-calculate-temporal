package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;

import java.util.List;

public interface IPeriodoEvaluacionServices {

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacions(String perEstado);

    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorId(Integer perId, String perEstado);

    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorNombre(String perNombre);

    public Integer cantidadPeriodoEvaluacions(String perEstado);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsNombres(String perTipo);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltro(String perEstado, String texto);

    public void crearPeriodoEvaluacion (PeriodoEvaluacion periodoEvaluacion);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltroPaginas(String perEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasPeriodoEvaluacions(String perEstado,  String texto);

    public PeriodoEvaluacion borrarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion);

    public PeriodoEvaluacion actualizarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion);


}
