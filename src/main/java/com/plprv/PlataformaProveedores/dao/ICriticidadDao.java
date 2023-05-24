package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Criticidad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICriticidadDao extends CrudRepository<Criticidad , Long> {
    public List<Criticidad> findByCrtEstadoAndIdEmppalOrderByCrtNombreAsc(String crtEstado, Integer idEmppal);

    public Criticidad findByCrtNombreAndIdEmppal(String crtNombre, Integer idEmppal);

    public Criticidad findByCrtIdAndIdEmppal(Integer crtId, Integer idEmppal);


    public Criticidad findByCrtIdAndCrtEstadoAndIdEmppal(Integer crtId, String crtEstado, Integer idEmppal);

    @Query("SELECT p FROM Criticidad p WHERE (p.crtEstado = :crtEstado) AND (p.idEmppal = :idEmppal) order by p.crtNombre ASC")
    List<Criticidad> findByCrtEstadoNombre(String crtEstado, Integer idEmppal);

    @Query("SELECT p FROM Criticidad p WHERE (lower(p.crtNombre) LIKE %:texto% or lower(p.crtDetalle) LIKE %:texto% ) AND p.crtEstado = :crtEstado " +
            " AND (p.idEmppal = :idEmppal) ORDER BY p.crtId DESC")
    List<Criticidad> findByCrtEstadoFiltro(String crtEstado, String texto , Integer idEmppal);

    @Query("SELECT p FROM Criticidad p WHERE (lower(p.crtNombre) LIKE %:texto% or lower(p.crtDetalle) LIKE %:texto%)" +
            " AND p.crtEstado = :crtEstado AND (p.idEmppal = :idEmppal) ORDER BY p.crtId DESC ")
    List<Criticidad> findByCrtEstadoPaginaFiltro(String crtEstado, String texto, Integer idEmppal, Pageable pageable);
}
