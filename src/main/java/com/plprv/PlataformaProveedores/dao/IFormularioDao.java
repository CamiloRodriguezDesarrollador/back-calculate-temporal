package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Formulario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioDao extends CrudRepository<Formulario, Long> {

    public List<Formulario> findByForEstado(String forEstado);

    public Formulario findByForNombre(String forNombre);

    public Formulario findByForId(Integer forId);
    public Formulario findByForIdAndForEstado(Integer forId, String forEstado);

    @Query("SELECT p  FROM Formulario p WHERE (p.forEstado = :forEstado) order by p.forNombre ASC")
    List<Formulario> findByForEstadoNombre(String forEstado);

    @Query("SELECT p FROM Formulario p WHERE (lower(p.forNombre) LIKE %:texto% or lower(p.forDetalle) LIKE %:texto% or lower(p.forTipo) LIKE %:texto%) AND p.forEstado = :forEstado ORDER BY p.forId DESC")
    List<Formulario> findByForEstadoFiltro(String forEstado, String texto);

    @Query("SELECT p FROM Formulario p WHERE (lower(p.forNombre) LIKE %:texto% or lower(p.forDetalle) LIKE %:texto% or lower(p.forTipo) LIKE %:texto%) AND p.forEstado = :forEstado ORDER BY p.forId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Formulario> findByForEstadoPaginaFiltro(String forEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);



}
