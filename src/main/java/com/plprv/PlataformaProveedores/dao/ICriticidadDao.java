package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Criticidad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICriticidadDao extends CrudRepository<Criticidad , Long> {
    public List<Criticidad> findByCrtEstadoOrderByCrtNombreAsc(String crtEstado);

    public Criticidad findByCrtNombre(String crtNombre);

    public Criticidad findByCrtId(Integer crtId);


    public Criticidad findByCrtIdAndCrtEstado(Integer crtId, String crtEstado);

    @Query("SELECT p FROM Criticidad p WHERE (p.crtEstado = :crtEstado) order by p.crtNombre ASC")
    List<Criticidad> findByCrtEstadoNombre(String crtEstado);

    @Query("SELECT p FROM Criticidad p WHERE (lower(p.crtNombre) LIKE %:texto% or lower(p.crtDetalle) LIKE %:texto% ) AND p.crtEstado = :crtEstado ORDER BY p.crtId DESC")
    List<Criticidad> findByCrtEstadoFiltro(String crtEstado, String texto);

    @Query("SELECT p FROM Criticidad p WHERE (lower(p.crtNombre) LIKE %:texto% or lower(p.crtDetalle) LIKE %:texto%) AND p.crtEstado = :crtEstado ORDER BY p.crtId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Criticidad> findByCrtEstadoPaginaFiltro(String crtEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);
}
