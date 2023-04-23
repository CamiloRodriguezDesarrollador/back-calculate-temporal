package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Usuario;

import java.util.List;

public interface IUsuarioServices {

    public List<Usuario> encontrarUsuarios(String proEstado);

    public Usuario encontrarUsuariosPorId(Integer proId, String proEstado);

    public Usuario encontrarUsuariosPorNombre(String proNombre);

    public Integer cantidadUsuarios(String proEstado);

    public List<Usuario> encontrarUsuariosNombres(String proEstado);

    public List<Usuario> encontrarUsuariosFiltro(String proEstado, String texto);

    public void crearUsuario (Usuario Usuario);

    public List<Usuario> encontrarUsuariosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina);

    public Integer cantidadPaginasUsuarios(String proEstado,  String texto);

    public Usuario borrarUsuario(Usuario Usuario);

    public Usuario actualizarUsuario(Usuario Usuario);




}
