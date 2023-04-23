package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProveedorEvaDao extends CrudRepository<ProveedorEva, Long> {
    public ProveedorEva findByPreId(Integer preId);

    public ProveedorEva findByPerIdAndPrvId(Integer perId, Integer prvId);

    public List<ProveedorEva> findByPrvId(Integer prvId);

    public List<ProveedorEva> findByPreEstado(String preEstado);

    public ProveedorEva findByPreIdAndPreEstado(Integer preId, String preEstado);

    public List<ProveedorEva> findByPerIdAndPreEstado(Integer perId, String preEstado);

//    @Query("SELECT p  FROM ProveedorEva p WHERE (p.perId = :perId) AND (p.preEstado = 'I' or p.preEstado = 'C')")
//    List<ProveedorEva> encontrarProveedoresEstadosCalcular(Integer perId);
    @Query("SELECT new com.plprv.PlataformaProveedores.entity.DocumentosProveedor ( pd.prvId, p.prvNombre , p.proId, p.sprId, count(pd) , p.crtId , p.tdcTd )" +
            " FROM ProveedorEva pe JOIN ProveedorDoc " +
            "pd ON pe.prvId = pd.prvId JOIN FormularioProceso fp ON pd.fopId = fp.fopId  " +
            " JOIN Proveedor p ON pe.prvId = p.prvId WHERE (pe.perId = :perId) AND (pe.preEstado = 'I' or pe.preEstado = 'C') AND (fp.perId = :perId) GROUP BY pd.prvId ," +
            "p.prvNombre , p.proId, p.sprId, p.crtId , p.tdcTd")
    List<DocumentosProveedor> encontrarProveedoresEstadosCalcular(Integer perId);

    @Query("SELECT count(fp.fopId) FROM FormularioProceso fp " +
            "WHERE (fp.proId = :proId) AND (fp.perId = :perId ) AND (fp.sprId = :sprId) AND (fp.crtId = :crtId) AND (fp.tdcTd = :tdcTd) AND (fp.fopEstado = 'A')")
    Long encontrarCantidadFormulario(Integer perId, Integer proId, Integer sprId, Integer crtId, String tdcTd);

    @Query("SELECT p  FROM ProveedorEva p WHERE (p.preEstado = :preEstado) order by p.preId DESC")
    List<ProveedorEva> findByPreEstadoNombre(String preEstado);

    @Query("SELECT p FROM ProveedorEva p WHERE (lower(p.preEstado) LIKE %:texto% ) AND p.preEstado = :preEstado AND (p.perId = :perId OR :perId = 0) ORDER BY p.preId DESC")
    List<ProveedorEva> findByPreEstadoFiltro(String preEstado, String texto, Integer perId);

    @Query("SELECT p FROM ProveedorEva p WHERE (p.preResultado LIKE %:texto% ) AND p.preEstado = :preEstado AND (p.perId = :perId OR :perId = 0) ORDER BY p.preId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<ProveedorEva> findByPreEstadoPaginaFiltro(String preEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial, Integer perId);


}
