package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.service.IUsuarioServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerUsuarios(){
        List<Usuario> usuariosDb = usuarioService.encontrarUsuarios("A");
        if(usuariosDb!=null){
            return  new ResponseEntity<>(usuariosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/usuario")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = usuarioService.cantidadUsuarios(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String usuNombre = jsonNode.get("usu_nombre").asText().trim();
                    String usuDocumento = jsonNode.get("usu_documento").asText().trim();
                    String usuCorreo = jsonNode.get("usu_correo").asText().trim();
                    String usuContrasena = jsonNode.get("usu_contrasena").asText().trim();
                    String usuTipo = jsonNode.get("usu_tipo").asText().trim();
                    String usuRol = jsonNode.get("usu_rol").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isTextNormal(usuNombre) || !regexService.isTextNormal(usuDocumento) || !regexService.isMail(usuCorreo) ||
                            !regexService.isTextNormal(usuContrasena) || !regexService.isSelectText(usuTipo) || !regexService.isSelectText(usuRol)
                            || !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);


                    Usuario usuariosDb = usuarioService.encontrarUsuariosPorNombre(usuCorreo.toLowerCase());
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encodedPassword = passwordEncoder.encode(usuContrasena);

                    if(usuariosDb == null) {
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
                        Set<Date> conjuntoFechas = new HashSet<>();
                        conjuntoFechas.add(fechaActual);
                        miUsuario.setAudFecha(fechaActual);
                       miUsuario.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            usuarioService.crearUsuario(miUsuario);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miUsuario, HttpStatus.OK);
                    }else{
                        usuariosDb.setUsuEstado("A");
                        usuarioService.actualizarUsuario(usuariosDb);
                        return  new ResponseEntity<>("activado" , HttpStatus.OK);
                    }
                }catch (Exception e){
                    if (e.getMessage().contains("null")){
                        String data = "campos_incompletos";
                        return new ResponseEntity<>( data, HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>( e.getMessage(), HttpStatus.OK);
                    }
                }
            case "informacionTotal":
                Integer numeroDePagina = (Integer) requestBody.get("numeroDePagina");
                Integer numeroElementosPorPagina = (Integer) requestBody.get("numeroElementosPorPagina");
                String texto = (String) requestBody.get("texto");

                List<Usuario> usuariosTodosDb = usuarioService.encontrarUsuariosFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(usuariosTodosDb!=null){
                    return  new ResponseEntity<>(usuariosTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");

                Integer usuariosTodosDbC = usuarioService.cantidadPaginasUsuarios(checkBoxEstado,textoC);
                if(usuariosTodosDbC!=null){
                    return  new ResponseEntity<>(usuariosTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer usuId = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbI = usuarioService.encontrarUsuariosPorId(usuId,null);
                if(usuariosDbI!=null){
                    return  new ResponseEntity<>(usuariosDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer usuIdD = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbB =   usuarioService.encontrarUsuariosPorId(usuIdD,"A");
                if(usuariosDbB != null) {
                    usuariosDbB.setUsuEstado("I");
                    usuarioService.actualizarUsuario(usuariosDbB);
                    return  new ResponseEntity<>(usuariosDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer usuIdDA = (Integer) requestBody.get("usu_id");
                Usuario usuariosDbBA =   usuarioService.encontrarUsuariosPorId(usuIdDA,"I");

                if(usuariosDbBA != null) {
                    usuariosDbBA.setUsuEstado("A");
                    usuarioService.actualizarUsuario(usuariosDbBA);
                    return  new ResponseEntity<>(usuariosDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "usuarioSoloNombre":
                List<Usuario> usuariosDbS = usuarioService.encontrarUsuariosNombres("A");
                if(usuariosDbS!=null && !usuariosDbS.isEmpty()){
                    return  new ResponseEntity<>(usuariosDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/usuario")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String usuario = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(usuario);
        try {
            Integer usuId = jsonNode.get("usu_id").asInt();
            String usuNombre = jsonNode.get("usu_nombre").asText().trim();
            String usuDocumento = jsonNode.get("usu_documento").asText().trim();
            String usuCorreo = jsonNode.get("usu_correo").asText().trim();
            String usuRol = jsonNode.get("usu_rol").asText().trim();

            Usuario usuarioDb = usuarioService.encontrarUsuariosPorId(usuId, "A");

            if (!regexService.isTextNormal(usuNombre) || !regexService.isTextNormal(usuDocumento) || !regexService.isMail(usuCorreo) ||
                    !regexService.isPassword(usuRol)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (usuarioDb != null) {
                usuarioDb.setUsuNombre(usuNombre.toLowerCase());
                usuarioDb.setUsuDocumento(usuDocumento);
                usuarioDb.setUsuCorreo(usuCorreo.toLowerCase());
                usuarioDb.setUsuRol(usuRol.toLowerCase());
                try {
                    usuarioService.actualizarUsuario(usuarioDb);
                } catch (DataIntegrityViolationException e) {
                    if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                        String data = "dato_existente";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        String data = "error_sql";
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

}
