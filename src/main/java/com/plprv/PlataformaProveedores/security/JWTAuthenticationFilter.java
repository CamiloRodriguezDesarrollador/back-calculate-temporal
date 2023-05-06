package com.plprv.PlataformaProveedores.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.dao.IUsuarioDao;
import com.plprv.PlataformaProveedores.entity.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        AuthCredentials authCredentials = new AuthCredentials();

        try {
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        }catch (IOException e){
        }

        UsernamePasswordAuthenticationToken usernamePat = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                unwrapPassword(authCredentials.getContrasena()),
                Collections.emptyList()
        );

        try {
            Authentication miAut = getAuthenticationManager().authenticate(usernamePat);
            return miAut;
        } catch (AuthenticationException e) {
            return null;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername(), userDetails.getRole(), userDetails.getIdEmppal());

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();


        PrintWriter out = response.getWriter();
        out.print("{\"message\": \"autenticado\",");
        out.print("\"nombre\": \""+ userDetails.getNombre()+"\",");
        out.print("\"token\": \""+ token+"\",");
        out.print("\"idEmppal\": \""+ userDetails.getIdEmppal()+"\",");
        out.print("\"rol\": \""+ userDetails.getRole()+"\"}");

        out.flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

    public static String unwrapPassword(String wrappedPassword) {
        String salt = wrappedPassword.substring(wrappedPassword.length() - 10);

        String mixedPassword = "";
        for (int i = 0; i < wrappedPassword.length() - 10; i++) {
            int charCode = wrappedPassword.charAt(i);
            mixedPassword += (char) (charCode - 1000);
        }
        int startIndex = "proveedores860090-1".length();
        int endIndex = mixedPassword.length() - salt.length();
        return mixedPassword.substring(startIndex, endIndex);
    }
}
