package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.FormularioDetalle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFormularioDetalleDao extends CrudRepository<FormularioDetalle, Long> {

    public List<FormularioDetalle> findByFodEstadoOrderByFodNombre(String fodEstado);

    public FormularioDetalle findByFodNombreAndForId (String fodNombre, Integer forId);

    public FormularioDetalle findByFodId(Integer fodId);


    public FormularioDetalle findByFodIdAndFodEstado(Integer fodId, String fodEstado);

    public List<FormularioDetalle> findByForIdAndFodEstado(Integer forId, String fodEstado);

    @Query("SELECT p  FROM FormularioDetalle p WHERE (p.fodEstado = :fodEstado) order by p.fodNombre ASC")
    List<FormularioDetalle> findByFodEstadoNombre(String fodEstado);

    @Query("SELECT p FROM FormularioDetalle p WHERE (lower(p.fodNombre) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto%) AND p.fodEstado = :fodEstado AND (p.forId = :forId OR :forId = 0) ORDER BY p.fodId DESC")
    List<FormularioDetalle> findByFodEstadoFiltro(String fodEstado, String texto, Integer forId);

    @Query("SELECT p FROM FormularioDetalle p WHERE (lower(p.fodNombre) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto% or lower(p.fodAdjunto) LIKE %:texto%) AND p.fodEstado = :fodEstado AND (p.forId = :forId OR :forId = 0) ORDER BY p.fodId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<FormularioDetalle> findByFodEstadoPaginaFiltro(String fodEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial, Integer forId);



}
