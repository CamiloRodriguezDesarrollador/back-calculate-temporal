package com.plprv.PlataformaProveedores.security;

import com.plprv.PlataformaProveedores.dao.IUsuarioDao;
import com.plprv.PlataformaProveedores.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioDao usuarioDao;

    private Integer idEmppal = null;


    public Integer getIdEmppal() {
        return idEmppal;
    }

    public void setIdEmppal(Integer idEmppal) {
        this.idEmppal = idEmppal;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String correoSinSufijo = "";
        Integer idEmppal = 0;
        try{
            correoSinSufijo = email.substring(0, email.indexOf("*"));
            idEmppal = Integer.parseInt(email.substring(email.indexOf("*") + 1));

        } catch (Exception e) {
            correoSinSufijo = email;
        }

        try {
            Usuario usuario = usuarioDao.findByUsuCorreoAndIdEmppalAndUsuEstado(correoSinSufijo,idEmppal,"A");
            if (usuario!= null) return new UserDetailsImpl(usuario);
        } catch (Exception e) {
        }
        return null;
    }



}
