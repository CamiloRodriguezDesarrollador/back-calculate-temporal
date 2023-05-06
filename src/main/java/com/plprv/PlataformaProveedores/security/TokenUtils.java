package com.plprv.PlataformaProveedores.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

public class TokenUtils {


    private static final String ACCES_TOKEN_SECRET = "plataforma_proveedores_860060915-9-5940500";
    private static final Long ACCES_TOKEN_VALIDITY_SECONDS = 36_200L; //10 horas

    public static String createToken(String nombre, String email, String role, int idEmppal){
        long expirationTime = ACCES_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis()+expirationTime);

        Map<String,Object> extra = new HashMap<>();
        extra.put("nombre",nombre);
        extra.put("role",role);
        extra.put("idEmppal",idEmppal);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCES_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCES_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(claims.get("role").toString()));

            try{
                UsernamePasswordAuthenticationToken miUser  = new UsernamePasswordAuthenticationToken(email, null, authorities);
                return miUser;
            } catch (Exception e) {
                return null ;
            }
        } catch (ExpiredJwtException e) {

            return null;
        } catch (UnsupportedJwtException e) {

            return null;
        } catch (SignatureException e) {

            return null;
        } catch (IllegalArgumentException e) {

            return null;
        }catch (JwtException e) {

            return null;
        }
    }

    public static Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCES_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
    private static Key getSecretKey() {
        String secret = ACCES_TOKEN_SECRET;
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
