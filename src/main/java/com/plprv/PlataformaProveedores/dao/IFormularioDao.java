package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Formulario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioDao extends CrudRepository<Formulario, Long> {

    public List<Formulario> findByForEstadoAndIdEmppalOrderByForNombreAsc(String forEstado, Integer idEmppal);

    public Formulario findByForNombreAndIdEmppal(String forNombre, Integer idEmppal);

    public Formulario findByForIdAndIdEmppal(Integer forId, Integer idEmppal);
    public Formulario findByForIdAndForEstadoAndIdEmppal(Integer forId, String forEstado, Integer idEmppal);

    @Query("SELECT p  FROM Formulario p WHERE (p.forEstado = :forEstado) AND (p.idEmppal = :idEmppal) order by p.forNombre ASC")
    List<Formulario> findByForEstadoNombre(String forEstado, Integer idEmppal);

    @Query("SELECT p FROM Formulario p WHERE (lower(p.forNombre) LIKE %:texto% or lower(p.forDetalle) LIKE %:texto% or lower(p.forTipo) LIKE %:texto%) " +
            "AND p.forEstado = :forEstado AND (p.idEmppal = :idEmppal) ORDER BY p.forId DESC")
    List<Formulario> findByForEstadoFiltro(String forEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Formulario p WHERE (lower(p.forNombre) LIKE %:texto% or lower(p.forDetalle) LIKE %:texto% or lower(p.forTipo)" +
            " LIKE %:texto%) AND p.forEstado = :forEstado AND (p.idEmppal = :idEmppal) ORDER BY p.forId DESC")
    List<Formulario> findByForEstadoPaginaFiltro(String forEstado, String texto,  Integer idEmppal, Pageable pageable);



}
