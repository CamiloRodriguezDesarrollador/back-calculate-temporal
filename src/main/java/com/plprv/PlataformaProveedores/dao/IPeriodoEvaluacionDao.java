package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPeriodoEvaluacionDao extends CrudRepository<PeriodoEvaluacion, Long> {

    public List<PeriodoEvaluacion> findByPerEstadoAndIdEmppal(String perEstado, Integer idEmppal);

    public PeriodoEvaluacion findByPerNombreAndIdEmppal(String perNombre,Integer idEmppal);

    public PeriodoEvaluacion findByPerIdAndIdEmppal(Integer perId, Integer idEmppal);

    public PeriodoEvaluacion findByPerIdAndPerEstadoAndIdEmppal(Integer perId, String perEstado, Integer idEmppal);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE p.perTipo LIKE COALESCE(:perTipo, '%') AND (p.idEmppal = :idEmppal) ORDER BY p.perId DESC")
    List<PeriodoEvaluacion> findByPerEstadoNombre(String perTipo, Integer idEmppal);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE (lower(p.perNombre) LIKE %:texto% or lower(p.perVisibilidad) LIKE %:texto% or lower(p.perTipo) " +
            "LIKE %:texto% ) AND p.perEstado = :perEstado AND (p.idEmppal = :idEmppal) ORDER BY p.perId DESC")
    List<PeriodoEvaluacion> findByPerEstadoFiltro(String perEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM PeriodoEvaluacion p WHERE (lower(p.perNombre) LIKE %:texto% or lower(p.perVisibilidad) LIKE %:texto% or " +
            "lower(p.perTipo) LIKE %:texto% ) AND p.perEstado = :perEstado AND (p.idEmppal = :idEmppal) ORDER BY p.perId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<PeriodoEvaluacion> findByPerEstadoPaginaFiltro(String perEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial, Integer idEmppal);


}
