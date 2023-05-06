package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;

import java.util.List;

public interface IPeriodoEvaluacionServices {

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacions(String perEstado, Integer idEmppal);

    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorId(Integer perId, String perEstado, Integer idEmppal);

    public PeriodoEvaluacion encontrarPeriodoEvaluacionsPorNombre(String perNombre, Integer idEmppal);

    public Integer cantidadPeriodoEvaluacions(String perEstado, Integer idEmppal);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsNombres(String perTipo, Integer idEmppal);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltro(String perEstado, String texto, Integer idEmppal);

    public void crearPeriodoEvaluacion (PeriodoEvaluacion periodoEvaluacion);

    public List<PeriodoEvaluacion> encontrarPeriodoEvaluacionsFiltroPaginas(String perEstado, String texto, Integer numeroDePagina,
                                                                            Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasPeriodoEvaluacions(String perEstado,  String texto, Integer idEmppal);

    public PeriodoEvaluacion borrarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion);

    public PeriodoEvaluacion actualizarPeriodoEvaluacion(PeriodoEvaluacion periodoEvaluacion);


}
