package com.plprv.PlataformaProveedores.service;

import com.plprv.PlataformaProveedores.dao.IUsuarioDao;
import com.plprv.PlataformaProveedores.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServices implements IUsuarioServices {

    @Autowired
    private IUsuarioDao usuarioDao;
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuarios(String usuEstado, Integer idEmppal) {
        return (List<Usuario>) usuarioDao.findByUsuEstadoAndIdEmppal(usuEstado, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuariosPorNombre(String usuCorreo, Integer idEmppal, String usuEstado) {
        return (Usuario) usuarioDao.findByUsuCorreoAndIdEmppalAndUsuEstado(usuCorreo, idEmppal, usuEstado);
    }
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuariosPorNombreSin(String usuCorreo, Integer idEmppal) {
        return (Usuario) usuarioDao.findByUsuCorreoAndIdEmppal(usuCorreo, idEmppal);
    }
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuariosPorId(Integer usuId, String usuEstado, Integer idEmppal) {
        if (usuEstado == null) {
            return usuarioDao.findByUsuIdAndIdEmppal(usuId, idEmppal);
        } else {
            return usuarioDao.findByUsuIdAndUsuEstadoAndIdEmppal(usuId, usuEstado, idEmppal);
        }
    }
    @Transactional(readOnly = true)
    public Integer cantidadUsuarios(String usuEstado, Integer idEmppal) {
        List<Usuario> datos = (List<Usuario>) usuarioDao.findByUsuEstadoAndIdEmppal(usuEstado, idEmppal);
        if(datos==null){
            return 0;
        }
        int numeroDeElementos = datos.size();
        return numeroDeElementos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuariosNombres(String usuEstado, Integer idEmppal) {
        return (List<Usuario>) usuarioDao.findByUsuEstadoNombre(usuEstado, idEmppal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> encontrarUsuariosFiltro(String usuEstado, String texto, Integer idEmppal) {
        return (List<Usuario>) usuarioDao.findByUsuEstadoFiltro(usuEstado,texto.toLowerCase(), idEmppal);
    }

    @Override
    public List<Usuario> encontrarUsuariosFiltroPaginas(String usuEstado, String texto, Integer numeroDePagina, Integer numeroElementosPorPagina, Integer idEmppal) {
        if(numeroDePagina == null){
            numeroDePagina = 1;
        } else if (numeroDePagina < 1){
            numeroDePagina = 1;
        }
        Pageable pageable = PageRequest.of(numeroDePagina - 1, numeroElementosPorPagina, Sort.Direction.DESC, "usuId");
        return usuarioDao.findByUsuEstadoPaginaFiltro(usuEstado, texto.toLowerCase(), idEmppal, pageable);
    }

    @Override
    public void crearUsuario(Usuario usuario) {
        usuarioDao.save(usuario);
    }
    @Override
    public Integer cantidadPaginasUsuarios(String usuEstado, String texto, Integer idEmppal) {
        List<Usuario> datos = (List<Usuario>) usuarioDao.findByUsuEstadoFiltro(usuEstado,texto.toLowerCase(), idEmppal);
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
