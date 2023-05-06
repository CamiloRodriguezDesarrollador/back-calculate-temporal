package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Autenticacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAutenticacionDao extends CrudRepository<Autenticacion, Long> {

    public List<Autenticacion> findByAutEstadoAndIdEmppalOrderByAutCorreoAsc(String autCorreo, Integer idEmppal);

    public Autenticacion findByAutCorreoAndIdEmppal(String autCorreo, Integer idEmppal);

    public Autenticacion findByAutIdAndIdEmppal(Integer autId, Integer idEmppal);

    public Autenticacion findByAutCodigoCorreo(String autCodigoCorreo);

    public Autenticacion findByAutIdAndAutEstadoAndIdEmppal(Integer autId, String autEstado, Integer idEmppal);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autEstado) = :autEstado) AND (p.idEmppal = :idEmppal) order by p.autId ASC")
    List<Autenticacion> findByAutEstadoNombre(String autEstado, Integer idEmppal);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autCorreo) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or p.prvNd LIKE %:texto% " +
            " ) AND p.autEstado = :autEstado AND (p.idEmppal = :idEmppal) ORDER BY p.autId DESC")
    List<Autenticacion> findByAutEstadoFiltro(String autEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Autenticacion p WHERE (lower(p.autCorreo) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or p.prvNd LIKE %:texto% " +
            ") AND p.autEstado = :autEstado AND (p.idEmppal = :idEmppal) ORDER BY p.autId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Autenticacion> findByAutEstadoPaginaFiltro(String autEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial, Integer idEmppal);
}
