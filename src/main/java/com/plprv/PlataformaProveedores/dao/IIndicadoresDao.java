package com.plprv.PlataformaProveedores.dao;
import com.plprv.PlataformaProveedores.entity.Proveedor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IIndicadoresDao extends CrudRepository<Proveedor, Long> {

    @Query("SELECT COUNT(p) FROM Proveedor p WHERE (p.prvEstado = 'A') ")
    public Integer encontrarCantidadProveedores();
    @Query("SELECT COUNT(p) FROM Proceso p WHERE (p.proEstado = 'A') ")
    public Integer contarProcesos();

    @Query("SELECT COUNT(p) FROM ProveedorEva p")
    public Integer calcularPorcentaje();
    @Query("SELECT COUNT(p) FROM PeriodoEvaluacion p WHERE (p.perEstado = 'A') ")
    public Integer contarPeriodos();
    @Query("SELECT pe FROM ProveedorEva pe JOIN Proveedor p ON pe.prvId = p.prvId WHERE (p.crtId = :crtId OR :crtId = 0) AND (pe.perId = :perId OR :perId = 0) AND (p.proId = :proId OR :proId = 0)")
    public List<Object> obtenerInformacionProveedorCriticidadPeriodo(Integer crtId , Integer perId, Integer proId);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.IndicadorEstados (pe.preEstado, COUNT(pe.preEstado)) FROM ProveedorEva pe JOIN Proveedor p ON  " +
            "pe.prvId = p.prvId WHERE (p.crtId = :crtId OR :crtId = 0) " +
            "AND (pe.perId = :perId OR :perId = 0)  AND (p.proId = :proId OR :proId = 0) GROUP BY pe.preEstado")
    public List<Object> contarRegistrosPorCriticidad(Integer crtId, Integer perId, Integer proId);

    @Query("SELECT DISTINCT (COUNT(pe)) FROM Proveedor p JOIN ProveedorEva pe ON pe.prvId = p.prvId WHERE (p.crtId = :crtId OR :crtId = 0) " +
            "AND (pe.perId = :perId OR :perId = 0) AND (p.proId = :proId OR :proId = 0) AND pe.preEstado='A'")
    public Integer cantidadProveedoresFiltro(Integer crtId, Integer perId , Integer proId);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.IndicadorProcesos ( pr.proNombre, COUNT(pr.proId)) FROM ProveedorEva pe JOIN Proveedor p " +
            "ON pe.prvId = p.prvId JOIN Proceso pr ON pr.proId = p.proId WHERE (p.crtId = :crtId OR :crtId = 0) AND (pe.perId = :perId OR :perId = 0) " +
            "AND (p.proId = :proId OR :proId = 0) GROUP BY pr.proNombre")
    public List<Object> contarProcesosPorFiltro(Integer crtId, Integer perId, Integer proId);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.IndicadorDocumentos ( pd.prdEstadoDocumental, COUNT(pd.prdId)) FROM ProveedorDoc pd JOIN Proveedor " +
            "p ON pd.prvId = p.prvId JOIN FormularioProceso fp ON fp.fopId = pd.fopId JOIN FormularioDetalle fd ON fp.fodId = fd.fodId " +
            " WHERE (p.crtId = :crtId OR :crtId = 0) AND (fp.perId = :perId OR :perId = 0)" +
            " AND (p.proId = :proId OR :proId = 0) AND (fd.fodTipo = 'file') GROUP BY pd.prdEstadoDocumental")
    public List<Object> contarDocumentosPorFiltro(Integer crtId, Integer perId, Integer proId);

    @Query("SELECT (COUNT(pe)) FROM Proveedor p JOIN ProveedorEva pe ON pe.prvId = p.prvId WHERE (p.crtId = :crtId OR :crtId = 0) " +
            "AND (pe.perId = :perId OR :perId = 0) AND (p.proId = :proId OR :proId = 0) AND pe.preEstado<>'NA'")
    public Double totalRegistros(Integer crtId, Integer perId, Integer proId);

    @Query("SELECT (COUNT(pe)) FROM Proveedor p JOIN ProveedorEva pe ON pe.prvId = p.prvId WHERE (p.crtId = :crtId OR :crtId = 0) " +
            "AND (pe.perId = :perId OR :perId = 0) AND (p.proId = :proId OR :proId = 0) AND (pe.preEstado= 'C' OR pe.preEstado='E')")
    public Double registrosCompleto(Integer crtId, Integer perId, Integer proId);
}

