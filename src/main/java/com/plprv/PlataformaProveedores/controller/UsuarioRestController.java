package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.security.UserDetailServiceImpl;
import com.plprv.PlataformaProveedores.service.IUsuarioServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ObtenerUsuarioAud;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UsuarioRestController {
    @Autowired
    private IUsuarioServices usuarioService;

    @Autowired
    private IRegexService regexService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    @Autowired
    private UserDetailServiceImpl userDetailsService;
    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador')")
    public ResponseEntity<?> obtenerUsuarios(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<Usuario> usuariosDb = usuarioService.encontrarUsuarios("A",miIdEmppal);
        if(usuariosDb!=null){
            return  new ResponseEntity<>(usuariosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/usuario")
    @PreAuthorize("hasAnyAuthority('superadministrador')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion) {
            case "cantidad" : {
                Integer cantidad = usuarioService.cantidadUsuarios(checkBoxEstado,miIdEmppal);
                return ResponseEntity.ok(cantidad);
            }
            case "crear" : {
                try {
                    int idEmppal = jsonNode.get("id_emppal").asInt();
                    String usuNombre = jsonNode.get("usu_nombre").asText().trim();
                    String usuDocumento = jsonNode.get("usu_documento").asText().trim();
                    String usuCorreo = jsonNode.get("usu_correo").asText().trim();
                    String usuContrasena = jsonNode.get("usu_contrasena").asText().trim();
                    String usuTipo = jsonNode.get("usu_tipo").asText().trim();
                    String usuRol = jsonNode.get("usu_rol").asText().trim();

                    if (!regexService.isTextNormal(usuNombre) || !regexService.isTextNormal(usuDocumento) || !regexService.isMail(usuCorreo) ||
                            !regexService.isTextNormal(usuContrasena) || !regexService.isSelectText(usuTipo) || !regexService.isSelectText(usuRol)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);


                    Usuario usuariosDb = usuarioService.encontrarUsuariosPorNombreSin(usuCorreo.toLowerCase(),miIdEmppal);
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encodedPassword = passwordEncoder.encode(usuContrasena);

                    if (usuariosDb == null) {
                        Usuario miUsuario = new Usuario();
                        miUsuario.setIdEmppal(idEmppal);
                        miUsuario.setUsuNombre(usuNombre.toLowerCase());
                        miUsuario.setUsuDocumento(usuDocumento);
                        miUsuario.setUsuCorreo(usuCorreo.toLowerCase());
                        miUsuario.setUsuContrasena(encodedPassword);
                        miUsuario.setUsuTipo(usuTipo.toLowerCase());
                        miUsuario.setUsuRol(usuRol.toLowerCase());
                        miUsuario.setUsuEstado("A");
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miUsuario.setAudFecha(fechaActual);
                        miUsuario.setAudUsuario(miAud);
                        try {
                            usuarioService.crearUsuario(miUsuario);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miUsuario, HttpStatus.OK);
                    } else {
                        usuariosDb.setUsuEstado("A");
                        usuarioService.actualizarUsuario(usuariosDb);
                        return new ResponseEntity<>("activado", HttpStatus.OK);
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains("null")) {
                        String data = "campos_incompletos";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
                    }
                }
            }
            case "informacionTotal" : {
                Integer numeroDePagina = (Integer) requestBody.get("numeroDePagina");
                Integer numeroElementosPorPagina = (Integer) requestBody.get("numeroElementosPorPagina");
                String texto = (String) requestBody.get("texto");
                List<Usuario> usuariosTodosDb = usuarioService.encontrarUsuariosFiltroPaginas(checkBoxEstado, texto, numeroDePagina, numeroElementosPorPagina,miIdEmppal);
                if (usuariosTodosDb != null) {
                    return new ResponseEntity<>(usuariosTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" : {
                String textoC = (String) requestBody.get("texto");
                Integer usuariosTodosDbC = usuarioService.cantidadPaginasUsuarios(checkBoxEstado, textoC,miIdEmppal);
                if (usuariosTodosDbC != null) {
                    return new ResponseEntity<>(usuariosTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" : {
                Integer usuId = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbI = usuarioService.encontrarUsuariosPorId(usuId, null,miIdEmppal);
                if (usuariosDbI != null) {
                    return new ResponseEntity<>(usuariosDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "borrar" : {
                Integer usuIdD = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbB = usuarioService.encontrarUsuariosPorId(usuIdD, "A",miIdEmppal);
                if (usuariosDbB != null) {
                    usuariosDbB.setUsuEstado("I");
                    usuarioService.actualizarUsuario(usuariosDbB);
                    return new ResponseEntity<>(usuariosDbB, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "activar" : {
                Integer usuIdDA = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbBA = usuarioService.encontrarUsuariosPorId(usuIdDA, "I",miIdEmppal);
                if (usuariosDbBA != null) {
                    usuariosDbBA.setUsuEstado("A");
                    usuarioService.actualizarUsuario(usuariosDbBA);
                    return new ResponseEntity<>(usuariosDbBA, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "usuarioSoloNombre" : {
                List<Usuario> usuariosDbS = usuarioService.encontrarUsuariosNombres("A",miIdEmppal);
                if (usuariosDbS != null && !usuariosDbS.isEmpty()) {
                    return new ResponseEntity<>(usuariosDbS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }
    }

    @PostMapping("/usuarioL")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        String miCorreo =  obtenerUsuarioAud.obtnerUsuarioToken(token);
        String opcion = (String) requestBody.get("opcion");

        switch (opcion) {
            case "obtenerUsuarioPorToken" : {
                Usuario usuario = usuarioService.encontrarUsuariosPorNombre(miCorreo,miIdEmppal,"A");
                if (usuario != null) {
                    return new ResponseEntity<>(usuario, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }

            case "editarContrasena" : {
                String contrasenaAntigua = (String) requestBody.get("prv_contrasena");
                contrasenaAntigua = unwrapPassword(contrasenaAntigua);
                String contrasenaNueva = (String) requestBody.get("prv_contrasenaN");
                contrasenaNueva = unwrapPassword(contrasenaNueva);
                if (!regexService.isPassword(contrasenaNueva) || !regexService.isPassword(contrasenaAntigua)
                ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPasswordN = passwordEncoder.encode(contrasenaNueva);

                Usuario miUsuario = usuarioService.encontrarUsuariosPorNombre(miCorreo,miIdEmppal,"A");
                if (miUsuario != null) {
                    String miContrasenaActual = miUsuario.getUsuContrasena();
                    if (passwordEncoder.matches(contrasenaAntigua, miContrasenaActual)) {
                        miUsuario.setUsuContrasena(encodedPasswordN);
                        usuarioService.actualizarUsuario(miUsuario);
                        return new ResponseEntity<>("modificado", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("contrasena_no_conincide", HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }

            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

    @PutMapping("/usuario")
    @PreAuthorize("hasAnyAuthority('superadministrador')")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String usuario = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(usuario);
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        try {
            Integer usuId = jsonNode.get("usu_id").asInt();
            String usuNombre = jsonNode.get("usu_nombre").asText().trim();
            String usuDocumento = jsonNode.get("usu_documento").asText().trim();
//            String usuCorreo = jsonNode.get("usu_correo").asText().trim();
            String usuRol = jsonNode.get("usu_rol").asText().trim();

            Usuario usuarioDb = usuarioService.encontrarUsuariosPorId(usuId, "A",miIdEmppal);

            if (!regexService.isTextNormal(usuNombre) || !regexService.isTextNormal(usuDocumento) ||
                    !regexService.isPassword(usuRol)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (usuarioDb != null) {
                usuarioDb.setUsuNombre(usuNombre.toLowerCase());
                usuarioDb.setUsuDocumento(usuDocumento);
//                usuarioDb.setUsuCorreo(usuCorreo.toLowerCase());
                usuarioDb.setUsuRol(usuRol.toLowerCase());
                try {
                    usuarioService.actualizarUsuario(usuarioDb);
                } catch (DataIntegrityViolationException e) {
                    if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                        String data = "dato_existente";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(((SQLException) e.getCause().getCause()).getErrorCode(), HttpStatus.OK);
                    }
                }
                String data = "editado";
                return new ResponseEntity<>(data, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        }catch (Exception e){
            if (e.getMessage().contains("null")){
                String data = "campos_incompletos";
                return new ResponseEntity<>( data, HttpStatus.OK);
            }else{
                return new ResponseEntity<>( e.getMessage(), HttpStatus.OK);
            }
        }
    }

    public static String unwrapPassword(String wrappedPassword) {
        String salt = wrappedPassword.substring(wrappedPassword.length() - 10);

        StringBuilder mixedPassword = new StringBuilder();
        for (int i = 0; i < wrappedPassword.length() - 10; i++) {
            int charCode = wrappedPassword.charAt(i);
            mixedPassword.append((char) (charCode - 1000));
        }
        int startIndex = "proveedores860090-1".length();
        int endIndex = mixedPassword.length() - salt.length();
        return mixedPassword.substring(startIndex, endIndex);
    }

}
