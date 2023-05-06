package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.entity.Usuario;

import java.util.List;

public interface IUsuarioServices {

    public List<Usuario> encontrarUsuarios(String proEstado, Integer idEmppal);

    public Usuario encontrarUsuariosPorId(Integer proId, String proEstado, Integer idEmppal);

    public Usuario encontrarUsuariosPorNombre(String proNombre, Integer idEmppal);

    public Integer cantidadUsuarios(String proEstado, Integer idEmppal);

    public List<Usuario> encontrarUsuariosNombres(String proEstado, Integer idEmppal);

    public List<Usuario> encontrarUsuariosFiltro(String proEstado, String texto, Integer idEmppal);

    public void crearUsuario (Usuario Usuario);

    public List<Usuario> encontrarUsuariosFiltroPaginas(String proEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal);

    public Integer cantidadPaginasUsuarios(String proEstado,  String texto, Integer idEmppal);

    public Usuario borrarUsuario(Usuario Usuario);

    public Usuario actualizarUsuario(Usuario Usuario);




}
