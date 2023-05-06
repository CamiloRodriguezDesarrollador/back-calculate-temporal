package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProveedorDao extends CrudRepository<Proveedor, Long> {
    public List<Proveedor> findByPrvEstadoAndIdEmppal(String prvEstado, Integer idEmppal);

    public Proveedor findByPrvIdAndIdEmppal(Integer prvId, Integer idEmppal);

    public Proveedor findByPrvNombreAndIdEmppal(String prvNombre, Integer idEmppal);

    public Proveedor findByPrvCorreoAndIdEmppal(String prvCorreo, Integer idEmppal);

    public Proveedor findByPrvTokenAndIdEmppal(String prvToken, Integer idEmppal);

    public Proveedor findByPrvNdAndTdcTdAndIdEmppal(String prvNd , String tdcTd, Integer idEmppal);

    public Proveedor findByPrvIdAndPrvEstadoAndIdEmppal(Integer prvId, String prvEstado, Integer idEmppal);

    public List<Proveedor> findByPrvEstadoAndCrtIdAndIdEmppal(String prvEstado, Integer crtId, Integer idEmppal);

    public Proveedor findByPrvTokenAndPrvEstadoAndIdEmppal(String prvToken, String prvEstado, Integer idEmppal);

    @Query("SELECT p  FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto%) AND " +
            "p.prvEstado = :prvEstado AND (p.idEmppal = :idEmppal) order by p.prvNombre ASC")
    List<Proveedor> findByPrvEstadoNombre(String prvEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or lower(p.prvCorreo) LIKE %:texto%) " +
            "AND p.prvEstado = :prvEstado AND (p.idEmppal = :idEmppal) ORDER BY p.prvId DESC")
    List<Proveedor> findByPrvEstadoFiltro(String prvEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or lower(p.prvCorreo) LIKE %:texto%) " +
            "AND p.prvEstado = :prvEstado AND (p.idEmppal = :idEmppal) ORDER BY p.prvId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Proveedor> findByPrvEstadoPaginaFiltro(String prvEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial , Integer idEmppal);
}
