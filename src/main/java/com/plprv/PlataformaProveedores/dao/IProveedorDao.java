package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProveedorDao extends CrudRepository<Proveedor, Long> {
    public List<Proveedor> findByPrvEstado(String prvEstado);

    public Proveedor findByPrvId(Integer prvId);

    public Proveedor findByPrvNombre(String prvNombre);

    public Proveedor findByPrvNdAndTdcTd(String prvNd , String tdcTd);

    public Proveedor findByPrvIdAndPrvEstado(Integer prvId, String prvEstado);

    public List<Proveedor> findByPrvEstadoAndCrtId(String prvEstado, Integer crtId);

    public Proveedor findByPrvTokenAndPrvEstado(String prvToken, String prvEstado);

    @Query("SELECT p  FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto%) AND " +
            "p.prvEstado = :prvEstado order by p.prvNombre ASC")
    List<Proveedor> findByPrvEstadoNombre(String prvEstado, String texto);

    @Query("SELECT p FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or lower(p.prvCorreo) LIKE %:texto%) AND p.prvEstado = :prvEstado ORDER BY p.prvId DESC")
    List<Proveedor> findByPrvEstadoFiltro(String prvEstado, String texto);

    @Query("SELECT p FROM Proveedor p WHERE (lower(p.prvNd) LIKE %:texto% or lower(p.prvNombre) LIKE %:texto% or lower(p.prvCorreo) LIKE %:texto%) AND p.prvEstado = :prvEstado ORDER BY p.prvId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Proveedor> findByPrvEstadoPaginaFiltro(String prvEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);
}
