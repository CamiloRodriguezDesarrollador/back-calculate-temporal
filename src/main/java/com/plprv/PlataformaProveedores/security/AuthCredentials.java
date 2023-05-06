package com.plprv.PlataformaProveedores.security;

import lombok.Data;

@Data
public class AuthCredentials {
    private String email;
    private String contrasena;
    private Integer idEmppal;



}
