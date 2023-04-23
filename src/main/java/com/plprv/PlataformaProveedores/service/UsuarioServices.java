package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IUsuarioDao;
import com.plprv.PlataformaProveedores.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServices implements IUsuarioServices {

    @Autowired
    private IUsuarioDao usuarioDao;
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuarios(String usuEstado) {
        return (List<Usuario>) usuarioDao.findByUsuEstado(usuEstado);
    }
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuariosPorNombre(String usuCorreo) {
        return (Usuario) usuarioDao.findByUsuCorreo(usuCorreo);
    }
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuariosPorId(Integer usuId, String usuEstado) {
        if (usuEstado == null) {
            return usuarioDao.findByUsuId(usuId);
        } else {
            return usuarioDao.findByUsuIdAndUsuEstado(usuId, usuEstado);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadUsuarios(String usuEstado) {
        List<Usuario> datos = (List<Usuario>) usuarioDao.findByUsuEstado(usuEstado);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuariosNombres(String usuEstado) {
        return (List<Usuario>) usuarioDao.findByUsuEstadoNombre(usuEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuariosFiltro(String usuEstado, String texto) {
        return (List<Usuario>) usuarioDao.findByUsuEstadoFiltro(usuEstado,texto.toLowerCase());
    }

    @Override
    public List<Usuario> encontrarUsuariosFiltroPaginas(String usuEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        }else if (numeroDePagina<1){
            numeroDePagina = 1;
        }
        Integer limiteInicial = (numeroDePagina-1)*(numeroElementosPorPagina);
        return (List<Usuario>) usuarioDao.findByUsuEstadoPaginaFiltro(usuEstado,texto.toLowerCase(),numeroElementosPorPagina,limiteInicial);
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        usuarioDao.save(usuario);
    }
    @Override
    public Integer cantidadPaginasUsuarios(String usuEstado, String texto) {
        List<Usuario> datos = (List<Usuario>) usuarioDao.findByUsuEstadoFiltro(usuEstado,texto.toLowerCase());
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    public Usuario borrarUsuario(Usuario usuario) {
        return (Usuario) usuarioDao.save(usuario);
    }
    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return (Usuario) usuarioDao.save(usuario);

    }
}
