package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProcesoDao extends CrudRepository<Proceso, Long> {

    public List<Proceso> findByProEstado(String proEstado);

    public Proceso findByProNombre(String proNombre);

    public Proceso findByProId(Integer proId);

    public Proceso findByProIdAndProEstado(Integer proId, String proEstado);

    @Query("SELECT p FROM Proceso p WHERE (p.proEstado = :proEstado) order by p.proNombre ASC")
    List<Proceso> findByProEstadoNombre(String proEstado);

    @Query("SELECT p FROM Proceso p WHERE (lower(p.proNombre) LIKE %:texto% or lower(p.proResponsable) LIKE %:texto% or lower(p.proCelular) LIKE %:texto% or lower(p.proCargo) LIKE %:texto% " +
            "or lower(p.proCargo) LIKE %:texto% or lower(p.proCorreo) LIKE %:texto%) AND p.proEstado = :proEstado ORDER BY p.proId DESC")
    List<Proceso> findByProEstadoFiltro(String proEstado, String texto);

    @Query("SELECT p FROM Proceso p WHERE (lower(p.proNombre) LIKE %:texto% or lower(p.proResponsable) LIKE %:texto% or lower(p.proCelular) LIKE %:texto% or lower(p.proCargo) LIKE %:texto% " +
            "or lower(p.proCargo) LIKE %:texto% or lower(p.proCorreo) LIKE %:texto%) AND p.proEstado = :proEstado ORDER BY p.proId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Proceso> findByProEstadoPaginaFiltro(String proEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);



}
