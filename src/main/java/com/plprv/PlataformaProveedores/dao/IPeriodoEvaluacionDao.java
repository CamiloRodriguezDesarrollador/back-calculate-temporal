package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPeriodoEvaluacionDao extends CrudRepository<PeriodoEvaluacion, Long> {

    public List<PeriodoEvaluacion> findByPerEstado(String perEstado);

    public PeriodoEvaluacion findByPerNombre(String perNombre);

    public PeriodoEvaluacion findByPerId(Integer perId);

    public PeriodoEvaluacion findByPerIdAndPerEstado(Integer perId, String perEstado);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE p.perTipo LIKE COALESCE(:perTipo, '%') ORDER BY p.perId DESC")
    List<PeriodoEvaluacion> findByPerEstadoNombre(String perTipo);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE (lower(p.perNombre) LIKE %:texto% or lower(p.perVisibilidad) LIKE %:texto% or lower(p.perTipo) LIKE %:texto% ) AND p.perEstado = :perEstado ORDER BY p.perId DESC")
    List<PeriodoEvaluacion> findByPerEstadoFiltro(String perEstado, String texto);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE (lower(p.perNombre) LIKE %:texto% or lower(p.perVisibilidad) LIKE %:texto% or lower(p.perTipo) LIKE %:texto% ) AND p.perEstado = :perEstado ORDER BY p.perId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<PeriodoEvaluacion> findByPerEstadoPaginaFiltro(String perEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);


}
