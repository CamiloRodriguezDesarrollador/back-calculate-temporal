package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.entity.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    public List<Usuario> findByUsuEstadoAndIdEmppal(String usuEstado, Integer idEmppal);

    public Usuario findByUsuCorreoAndIdEmppalAndUsuEstado(String usuCorreo, Integer inEmppal, String usuEstado);

    public Usuario findByUsuCorreoAndIdEmppal(String usuCorreo, Integer inEmppal);

    public Usuario findByUsuCorreo(String usuCorreo);
    public Usuario findByUsuIdAndIdEmppal(Integer usuId, Integer idEmppal);

    public Usuario findByUsuIdAndUsuEstadoAndIdEmppal(Integer usuId, String usuEstado, Integer idEmppal);

    @Query("SELECT p FROM Usuario p WHERE (p.usuEstado = :usuEstado) AND (p.idEmppal = :idEmppal) order by p.usuCorreo ASC")
    List<Usuario> findByUsuEstadoNombre(String usuEstado, Integer idEmppal);

    @Query("SELECT p FROM Usuario p WHERE (lower(p.usuCorreo) LIKE %:texto% or lower(p.usuNombre) LIKE %:texto% or lower(p.usuDocumento) " +
            "LIKE %:texto% or lower(p.usuRol) LIKE %:texto% " +
            ") AND p.usuEstado = :usuEstado AND (p.idEmppal = :idEmppal) ORDER BY p.usuId DESC")
    List<Usuario> findByUsuEstadoFiltro(String usuEstado, String texto, Integer idEmppal);

    @Query("SELECT p FROM Usuario p WHERE (lower(p.usuCorreo) LIKE %:texto% or lower(p.usuNombre) LIKE %:texto% or lower(p.usuDocumento) LIKE %:texto% or lower(p.usuRol) LIKE %:texto%) " +
            "AND p.usuEstado = :usuEstado AND p.usuTipo <> 'proveedor' AND (p.idEmppal = :idEmppal) ORDER BY p.usuId DESC")
    List<Usuario> findByUsuEstadoPaginaFiltro(String usuEstado, String texto, Integer idEmppal, Pageable pageable);

}
