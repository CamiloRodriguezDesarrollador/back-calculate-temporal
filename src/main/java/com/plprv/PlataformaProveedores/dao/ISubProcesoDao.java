package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISubProcesoDao extends CrudRepository<SubProceso, Long> {

    public List<SubProceso> findBySprEstado(String sprEstado);

    public SubProceso findBySprNombreAndProId(String sprNombre, Integer proId);

    public SubProceso findBySprId(Integer sprId);

    public SubProceso findBySprIdAndSprEstado(Integer sprId, String sprEstado);

    @Query("SELECT p  FROM SubProceso p WHERE (p.sprEstado = :sprEstado) order by p.proId ASC , p.sprNombre ASC ")
    List<SubProceso> findBySprEstadoNombre(String sprEstado);

    @Query("SELECT p FROM SubProceso p WHERE (lower(p.sprNombre) LIKE %:texto%) AND (p.sprEstado = :sprEstado) AND (p.proId = :proId OR :proId = 0)  ORDER BY p.sprId DESC")
    List<SubProceso> findBySprEstadoFiltro(String sprEstado, String texto , Integer proId);

    @Query("SELECT p FROM SubProceso p WHERE (lower(p.sprNombre) LIKE %:texto%) AND (p.sprEstado = :sprEstado) AND (p.proId = :proId OR :proId = 0) ORDER BY p.sprId DESC LIMIT" +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<SubProceso> findBySprEstadoPaginaFiltro(String sprEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial, Integer proId);


}
