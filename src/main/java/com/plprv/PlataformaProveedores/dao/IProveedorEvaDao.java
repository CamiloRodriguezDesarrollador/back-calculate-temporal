package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProveedorEvaDao extends CrudRepository<ProveedorEva, Long> {
    public ProveedorEva findByPreIdAndIdEmppal(Integer preId, Integer idEmppal);

    public ProveedorEva findByPerIdAndPrvIdAndIdEmppal(Integer perId, Integer prvId, Integer idEmppal);

    public List<ProveedorEva> findByPrvIdAndIdEmppal(Integer prvId, Integer idEmppal);

    public List<ProveedorEva> findByPreEstadoAndIdEmppal(String preEstado, Integer idEmppal);

    public ProveedorEva findByPreIdAndPreEstadoAndIdEmppal(Integer preId, String preEstado, Integer idEmppal);

    public List<ProveedorEva> findByPerIdAndPreEstadoAndIdEmppal(Integer perId, String preEstado, Integer idEmppal);

    @Query("SELECT new com.plprv.PlataformaProveedores.entity.DocumentosProveedor ( pd.prvId, p.prvNombre , p.proId, p.sprId, count(pd) , p.crtId , p.tdcTd )" +
            " FROM ProveedorEva pe JOIN ProveedorDoc " +
            "pd ON pe.prvId = pd.prvId JOIN FormularioProceso fp ON pd.fopId = fp.fopId  " +
            " JOIN Proveedor p ON pe.prvId = p.prvId WHERE (pe.perId = :perId) AND (pe.preEstado = 'I' or pe.preEstado = 'C') AND (fp.perId = :perId) " +
            "AND (pd.prdEstadoDocumental != 'P' ) AND (pe.idEmppal = :idEmppal)" +
            " GROUP BY pd.prvId ," +
            "p.prvNombre , p.proId, p.sprId, p.crtId , p.tdcTd")
    List<DocumentosProveedor> encontrarProveedoresEstadosCalcular(Integer perId, Integer idEmppal);

    @Query("SELECT count(fp.fopId) FROM FormularioProceso fp " +
            "WHERE (fp.proId = :proId) AND (fp.perId = :perId ) AND (fp.sprId = :sprId) AND (fp.crtId = :crtId) AND (fp.tdcTd = :tdcTd)" +
            " AND (fp.fopEstado = 'A' AND (fp.idEmppal = :idEmppal))")
    Long encontrarCantidadFormulario(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd, int idEmppal);

    @Query("SELECT p  FROM ProveedorEva p WHERE (p.preEstado = :preEstado) AND (p.idEmppal = :idEmppal) order by p.preId DESC")
    List<ProveedorEva> findByPreEstadoNombre(String preEstado, Integer idEmppal);

    @Query("SELECT p FROM ProveedorEva p WHERE (lower(p.preEstado) LIKE %:texto% ) AND p.preEstado = :preEstado AND" +
            " (p.perId = :perId OR :perId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.preId DESC")
    List<ProveedorEva> findByPreEstadoFiltro(String preEstado, String texto, Integer perId, Integer idEmppal);

    @Query("SELECT p FROM ProveedorEva p WHERE (p.preObservacion LIKE %:texto% ) AND p.preEstado = :preEstado" +
            " AND (p.perId = :perId OR :perId = 0) AND (p.idEmppal = :idEmppal) ORDER BY p.preId DESC")
    List<ProveedorEva> findByPreEstadoPaginaFiltro(String preEstado, String texto, Integer perId , Integer idEmppal, Pageable pageable);


}
