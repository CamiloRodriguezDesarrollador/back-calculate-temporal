package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.FormularioDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioDetalleDao extends CrudRepository<FormularioDetalle, Long> {

    public List<FormularioDetalle> findByFodEstadoAndIdEmppalOrderByFodNombre(String fodEstado, Integer idEmppal);

    public FormularioDetalle findByFodNombreAndForIdAndIdEmppal(String fodNombre, Integer forId, Integer idEmppal);

    public FormularioDetalle findByFodIdAndIdEmppal(Integer fodId, Integer idEmppal);


    public FormularioDetalle findByFodIdAndFodEstadoAndIdEmppal(Integer fodId, String fodEstado, Integer idEmppal);

    public List<FormularioDetalle> findByForIdAndFodEstadoAndIdEmppal(Integer forId, String fodEstado, Integer idEmppal);

    @Query("SELECT p  FROM FormularioDetalle p WHERE (p.fodEstado = :fodEstado) AND (p.idEmppal = :idEmppal) order by p.fodNombre ASC")
    List<FormularioDetalle> findByFodEstadoNombre(String fodEstado, Integer idEmppal);

    @Query("SELECT p FROM FormularioDetalle p WHERE (lower(p.fodNombre) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto% or lower(p.fodAdjunto)" +
            " LIKE %:texto%) AND p.fodEstado = :fodEstado AND (p.forId = :forId OR :forId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.fodId DESC")
    List<FormularioDetalle> findByFodEstadoFiltro(String fodEstado, String texto, Integer forId, Integer idEmppal);

    @Query("SELECT p FROM FormularioDetalle p WHERE (lower(p.fodNombre) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto% or lower(p.fodAdjunto)" +
            " LIKE %:texto%) AND p.fodEstado = :fodEstado AND (p.forId = :forId OR :forId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.fodId DESC")
    List<FormularioDetalle> findByFodEstadoPaginaFiltro(String fodEstado, String texto, Integer forId, Integer idEmppal, Pageable pageable);



}
