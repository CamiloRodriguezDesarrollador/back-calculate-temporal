package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProcesoDao extends CrudRepository<Proceso, Long> {

    public List<Proceso> findByProEstadoAndIdEmppalOrderByProNombreAsc(String proEstado, Integer idEmppal);

    public Proceso findByProNombreAndIdEmppal(String proNombre, Integer idEmppal);

    public Proceso findByProIdAndIdEmppal(Integer proId, Integer idEmppal);

    public Proceso findByProIdAndProEstadoAndIdEmppal(Integer proId, String proEstado, Integer idEmppal);

    @Query("SELECT p FROM Proceso p WHERE (p.proEstado = :proEstado) AND (p.idEmppal = :idEmppal) order by p.proNombre ASC")
    List<Proceso> findByProEstadoNombre(String proEstado, Integer idEmppal);

    @Query("SELECT p FROM Proceso p WHERE (lower(p.proNombre) LIKE %:texto% or lower(p.proResponsable) LIKE %:texto% or lower(p.proCelular) LIKE %:texto% or lower(p.proCargo) LIKE %:texto% " +
            "or lower(p.proCargo) LIKE %:texto% or lower(p.proCorreo) LIKE %:texto%) AND p.proEstado = :proEstado AND (p.idEmppal = :idEmppal) ORDER BY p.proId DESC")
    List<Proceso> findByProEstadoFiltro(String proEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Proceso p WHERE (lower(p.proNombre) LIKE %:texto% or lower(p.proResponsable) LIKE %:texto% or lower(p.proCelular) LIKE %:texto% or lower(p.proCargo) LIKE %:texto% " +
            "or lower(p.proCargo) LIKE %:texto% or lower(p.proCorreo) LIKE %:texto%) AND p.proEstado = :proEstado AND (p.idEmppal = :idEmppal) ORDER BY p.proId DESC ")
    List<Proceso> findByProEstadoPaginaFiltro(String proEstado, String texto, Integer idEmppal, Pageable pageable);



}
