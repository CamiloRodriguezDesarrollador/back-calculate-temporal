package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Autenticacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAutenticacionDao extends CrudRepository<Autenticacion, Long> {

    public List<Autenticacion> findByAutEstadoOrderByAutCorreoAsc(String autCorreo);

    public Autenticacion findByAutCorreo(String autCorreo);

    public Autenticacion findByAutId(Integer autId);

    public Autenticacion findByAutCodigoCorreo(String autCodigoCorreo);

    public Autenticacion findByAutIdAndAutEstado(Integer autId, String autEstado);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autEstado) = :autEstado) order by p.autId ASC")
    List<Autenticacion> findByAutEstadoNombre(String autEstado);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autCorreo) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or p.prvNd LIKE %:texto% " +
            " ) AND p.autEstado = :autEstado ORDER BY p.autId DESC")
    List<Autenticacion> findByAutEstadoFiltro(String autEstado, String texto);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autCorreo) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or p.prvNd LIKE %:texto% " +
            ") AND p.autEstado = :autEstado ORDER BY p.autId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Autenticacion> findByAutEstadoPaginaFiltro(String autEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);
}
