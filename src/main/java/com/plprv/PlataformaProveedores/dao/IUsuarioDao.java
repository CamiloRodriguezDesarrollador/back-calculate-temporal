package com.plprv.PlataformaProveedores.dao;

import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    public List<Usuario> findByUsuEstado(String usuEstado);

    public Usuario findByUsuCorreo(String usuCorreo);

    public Usuario findByUsuId(Integer usuId);

    public Usuario findByUsuIdAndUsuEstado(Integer usuId, String usuEstado);

    @Query("SELECT p FROM Usuario p WHERE (p.usuEstado = :usuEstado) order by p.usuCorreo ASC")
    List<Usuario> findByUsuEstadoNombre(String usuEstado);

    @Query("SELECT p FROM Usuario p WHERE (lower(p.usuCorreo) LIKE %:texto% or lower(p.usuNombre) LIKE %:texto% or lower(p.usuDocumento) LIKE %:texto% or lower(p.usuRol) LIKE %:texto% " +
            ") AND p.usuEstado = :usuEstado ORDER BY p.usuId DESC")
    List<Usuario> findByUsuEstadoFiltro(String usuEstado, String texto);

    @Query("SELECT p FROM Usuario p WHERE (lower(p.usuCorreo) LIKE %:texto% or lower(p.usuNombre) LIKE %:texto% or lower(p.usuDocumento) LIKE %:texto% or lower(p.usuRol) LIKE %:texto% " +
            ") AND p.usuEstado = :usuEstado ORDER BY p.usuId DESC LIMIT " +
            " :numeroElementosPorPagina OFFSET :limiteInicial")
    List<Usuario> findByUsuEstadoPaginaFiltro(String usuEstado, String texto, Integer numeroElementosPorPagina, Integer limiteInicial);


}
