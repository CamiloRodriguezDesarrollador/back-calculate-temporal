package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Documentos;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IProveedorDocDao extends CrudRepository<ProveedorDoc, Long> {
    public ProveedorDoc findByPrdIdAndIdEmppal(Integer prdId, Integer idEmppal);

    public List<ProveedorDoc> findByFopIdAndIdEmppal(Integer fopId, Integer idEmppal);

    public ProveedorDoc findByFopIdAndPrvIdAndIdEmppal(Integer fopId, Integer prvId, Integer idEmppal);

    public List<ProveedorDoc> findByPrvIdAndIdEmppal(Integer prvId, Integer idEmppal);


    @Query("SELECT new com.plprv.PlataformaProveedores.entity.Documentos (pd.prdId , pd.prdData,pd.prdObservacion,pd.prdFechaDocumento,pd.prdEstadoDocumental," +
            " p.tdcTd , p.prvNd, p.prvNombre , fo.forNombre, fd.fodNombre, fd.fodTipo , per.perNombre , per.perFechaEvaluacion , per.perTipo , pro.proNombre , spr.sprNombre ,p.audFecha,pd.audUsuario, " +
            " per.perEstado, crt.crtNombre , fd.fodVigencia , p.prvCorreo )" +
            " FROM ProveedorDoc pd JOIN Proveedor p ON pd.prvId = p.prvId JOIN FormularioProceso fp ON pd.fopId = fp.fopId JOIN PeriodoEvaluacion per ON " +
            " fp.perId = per.perId JOIN Proceso pro ON p.proId = pro.proId JOIN SubProceso spr ON p.sprId = spr.sprId JOIN FormularioDetalle fd ON fp.fodId = fd.fodId " +
            " JOIN Formulario fo ON fp.forId = fo.forId JOIN Criticidad crt ON p.crtId = crt.crtId  WHERE (p.crtId = :crtId OR :crtId = 0) AND (p.proId = :proId OR :proId = 0) AND (p.sprId = :sprId OR :sprId = 0)" +
            " AND (per.perId = :perId OR :perId = 0) AND (p.prvId = :prvId OR :prvId = 0) AND (fp.forId = :forId OR :forId = 0) " +
            " AND (lower(fd.fodNombre) LIKE %:texto%) AND (p.prvNd LIKE %:prvNd%) AND (lower(p.prvNombre) LIKE %:prvNombre%) AND (fd.fodTipo LIKE %:tipo%) " +
            "AND pd.prdEstadoDocumental = COALESCE(NULLIF(:prdEstadoDocumental, '') , pd.prdEstadoDocumental)  AND (pd.idEmppal = :idEmppal) ORDER BY pd.prdId")
    public List<Object> cantidadDocumentacionTabla(Integer prvId,Integer crtId, Integer forId, String texto , String prvNd, String prvNombre ,
                                                   Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental,
                                                   String tipo, Integer idEmppal, Pageable pageable);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.InformeGeneral ( " +
            "pe.perId, pe.prvId, p.tdcTd , p.prvNd, p.prvNombre ,  per.perNombre , per.perFechaEvaluacion , per.perTipo , pro.proNombre , spr.sprNombre ,pe.audFecha,pe.audUsuario, " +
            " crt.crtNombre , p.prvDireccion, p.prvCelular,p.ciuNombre,p.dptNombre,p.prvCorreo, p.prvCarpeta, p.audFecha" +
            " , pe.preEstado, pe.preContinua, pe.preObservacion,pe.preResultado)" +
            " FROM ProveedorEva pe JOIN Proveedor p ON pe.prvId = p.prvId JOIN PeriodoEvaluacion per ON pe.perId = per.perId JOIN Proceso pro ON p.proId = pro.proId JOIN SubProceso spr ON p.sprId = spr.sprId " +
            " JOIN Criticidad crt ON p.crtId = crt.crtId WHERE (p.crtId = :crtId OR :crtId = 0) AND (p.proId = :proId OR :proId = 0) AND (p.sprId = :sprId OR :sprId = 0)" +
            " AND (per.perId = :perId OR :perId = 0) AND (p.prvId = :prvId OR :prvId = 0) " +
            " AND (p.prvNd LIKE %:prvNd%) AND (lower(p.prvNombre) LIKE %:prvNombre%) AND (pe.idEmppal = :idEmppal)" +
            " AND pe.preEstado = COALESCE(NULLIF(:preEstado, ''), pe.preEstado) ORDER BY pe.perId DESC")
    public List<Object> cantidadEvaluacionTabla(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId,
                                                String preEstado, Integer idEmppal, Pageable pageable);


    @Query("SELECT new com.plprv.PlataformaProveedores.entity.Documentos ( pd.prdId , pd.prdData,pd.prdObservacion,pd.prdFechaDocumento,pd.prdEstadoDocumental," +
            " p.tdcTd , p.prvNd, p.prvNombre , fo.forNombre, fd.fodNombre, fd.fodTipo , per.perNombre , per.perFechaEvaluacion , per.perTipo , pro.proNombre , spr.sprNombre ,pd.audFecha,pd.audUsuario, " +
            "per.perEstado, crt.crtNombre , fd.fodVigencia , p.prvCorreo)" +
            " FROM ProveedorDoc pd JOIN Proveedor p ON pd.prvId = p.prvId JOIN FormularioProceso fp ON pd.fopId = fp.fopId JOIN PeriodoEvaluacion per ON " +
            " fp.perId = per.perId JOIN Proceso pro ON p.proId = pro.proId JOIN SubProceso spr ON p.sprId = spr.sprId JOIN FormularioDetalle fd ON fp.fodId = fd.fodId " +
            " JOIN Formulario fo ON fp.forId = fo.forId  JOIN Criticidad crt ON p.crtId = crt.crtId WHERE (p.crtId = :crtId OR :crtId = 0) " +
            " AND (p.proId = :proId OR :proId = 0)  AND (p.prvId = :prvId OR :prvId = 0) AND (fp.forId = :forId OR :forId = 0) AND (p.sprId = :sprId OR :sprId = 0) AND (per.perId = :perId OR :perId = 0) " +
            " AND (lower(fd.fodNombre) LIKE %:texto%) AND (p.prvNd LIKE %:prvNd%) AND (lower(p.prvNombre) LIKE %:prvNombre%)  AND (lower(fd.fodTipo) LIKE %:tipo%)" +
            "AND pd.prdEstadoDocumental = COALESCE(NULLIF(:prdEstadoDocumental, ''), pd.prdEstadoDocumental) AND (pd.idEmppal = :idEmppal)")
    public List<Documentos> cantidadDocumentacionTablaTodo(Integer prvId, Integer crtId , Integer forId, String texto, String prvNd, String prvNombre,
                                                           Integer perId, Integer proId, Integer sprId, String prdEstadoDocumental, String tipo, Integer idEmppal);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.InformeGeneral ( " +
            "pe.perId, pe.prvId, p.tdcTd , p.prvNd, p.prvNombre ,  per.perNombre , per.perFechaEvaluacion , per.perTipo , pro.proNombre , spr.sprNombre ,pe.audFecha,pe.audUsuario, " +
            " crt.crtNombre , p.prvDireccion, p.prvCelular,p.ciuNombre,p.dptNombre,p.prvCorreo, p.prvCarpeta, p.audFecha" +
            " , pe.preEstado, pe.preContinua, pe.preObservacion,pe.preResultado)" +
            " FROM ProveedorEva pe JOIN Proveedor p ON pe.prvId = p.prvId JOIN PeriodoEvaluacion per ON pe.perId = per.perId JOIN Proceso pro ON p.proId = pro.proId JOIN SubProceso spr ON p.sprId = spr.sprId " +
            " JOIN Criticidad crt ON p.crtId = crt.crtId WHERE (p.crtId = :crtId OR :crtId = 0) AND (p.proId = :proId OR :proId = 0) AND (p.sprId = :sprId OR :sprId = 0)" +
            " AND (per.perId = :perId OR :perId = 0) AND (p.prvId = :prvId OR :prvId = 0) " +
            " AND (p.prvNd LIKE %:prvNd%) AND (lower(p.prvNombre) LIKE %:prvNombre%) AND (pe.idEmppal = :idEmppal)" +
            " AND pe.preEstado = COALESCE(NULLIF(:preEstado, ''), pe.preEstado)")
    public List<Object> cantidadEvaluacionTablaTodo(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId,
                                                    Integer proId, Integer sprId, String preEstado, Integer idEmppal);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.InformeGeneral ( " +
            " pe.perId, pe.prvId, p.tdcTd , p.prvNd, p.prvNombre ,  per.perNombre , per.perFechaEvaluacion , per.perTipo , pro.proNombre , spr.sprNombre ,pe.audFecha,pe.audUsuario, " +
            " crt.crtNombre , p.prvDireccion, p.prvCelular,p.ciuNombre,p.dptNombre,p.prvCorreo, p.prvCarpeta, p.audFecha" +
            " , pe.preEstado, pe.preContinua, pe.preObservacion,pe.preResultado)" +
            " FROM ProveedorEva pe JOIN Proveedor p ON pe.prvId = p.prvId JOIN PeriodoEvaluacion per ON pe.perId = per.perId JOIN Proceso pro ON p.proId = pro.proId JOIN SubProceso spr ON p.sprId = spr.sprId " +
            " JOIN Criticidad crt ON p.crtId = crt.crtId WHERE (p.crtId = :crtId OR :crtId = 0) AND (p.proId = :proId OR :proId = 0) AND (p.sprId = :sprId OR :sprId = 0)" +
            " AND (per.perId = :perId OR :perId = 0) AND (p.prvId = :prvId OR :prvId = 0) " +
            " AND (p.prvNd LIKE %:prvNd%) AND (lower(p.prvNombre) LIKE %:prvNombre%) AND (pe.idEmppal = :idEmppal)" +
            " AND (pe.preEstado = COALESCE(NULLIF(:preEstado, ''), pe.preEstado)) ORDER BY pe.perId DESC")
    public List<Object> informeGeneral(Integer prvId,Integer crtId, String prvNd, String prvNombre , Integer perId, Integer proId, Integer sprId,
                                       String preEstado, Integer idEmppal);


}
