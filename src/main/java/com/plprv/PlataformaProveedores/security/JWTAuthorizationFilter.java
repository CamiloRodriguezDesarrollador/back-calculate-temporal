package com.plprv.PlataformaProveedores.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.service.IProveedorServices;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;


@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private IProveedorServices proveedorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && !bearerToken.startsWith("Bearer undefined")) {
                String token = bearerToken.replace("Bearer ", "");
                UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(usernamePAT);
                try {
                    String[] tokenParts = token.split("\\.");
                    String tokenPayload = tokenParts[1];
                    Base64.Decoder decoder = Base64.getDecoder();
                    String decodedPayload = new String(decoder.decode(tokenPayload));
                    JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();
                    String email = payloadJson.get("sub").getAsString();
                    Integer idEmppal = payloadJson.get("idEmppal").getAsInt();

                    Proveedor miProveedor = proveedorService.encontrarProveedorPorCorreo(email,idEmppal);
                    miProveedor.setPrvToken(token);
                    proveedorService.actualizarProveedor(miProveedor);
                } catch (Exception e) {
                }

            }else {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("sin_token"));
                Authentication authentication = new UsernamePasswordAuthenticationToken("sin_autenticacion", null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            try{
                filterChain.doFilter(request, response);
            } catch (IOException e) {
            } catch (ServletException e) {
            }
        } catch (Exception e) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado.");
        }
    }

    private void handleError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{ \"message\": \"" + message + "\" }");

    }

}