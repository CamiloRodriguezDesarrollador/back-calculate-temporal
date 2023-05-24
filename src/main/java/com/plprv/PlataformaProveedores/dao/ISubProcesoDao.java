package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISubProcesoDao extends CrudRepository<SubProceso, Long> {

    public List<SubProceso> findBySprEstadoAndIdEmppal(String sprEstado, Integer idEmppal);

    public SubProceso findBySprNombreAndProIdAndIdEmppal(String sprNombre, Integer proId, Integer idEmppal);

    public SubProceso findBySprIdAndIdEmppal(Integer sprId, Integer idEmppal);

    public SubProceso findBySprIdAndSprEstadoAndIdEmppal(Integer sprId, String sprEstado, Integer idEmppal);

    @Query("SELECT p  FROM SubProceso p WHERE (p.sprEstado = :sprEstado) AND (p.idEmppal = :idEmppal) order by p.proId ASC , p.sprNombre ASC ")
    List<SubProceso> findBySprEstadoNombre(String sprEstado, Integer idEmppal);

    @Query("SELECT p FROM SubProceso p WHERE (lower(p.sprNombre) LIKE %:texto%) AND (p.sprEstado = :sprEstado)" +
            " AND (p.proId = :proId OR :proId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.sprId DESC")
    List<SubProceso> findBySprEstadoFiltro(String sprEstado, String texto , Integer proId, Integer idEmppal);

    @Query("SELECT p FROM SubProceso p WHERE (lower(p.sprNombre) LIKE %:texto%) AND (p.sprEstado = :sprEstado)" +
            " AND (p.proId = :proId OR :proId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.sprId DESC")
    List<SubProceso> findBySprEstadoPaginaFiltro(String sprEstado, String texto,  Integer proId, Integer idEmppal, Pageable pageable);


}
