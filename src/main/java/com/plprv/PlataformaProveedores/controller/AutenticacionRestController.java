package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Autenticacion;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AutenticacionRestController {
    @Autowired
    private IAutenticacionServices autenticacionService;
    @Autowired
    private IRegexService regexService;
    @Autowired
    private IUsuarioServices usuarioService;
    @Autowired
    private IProveedorEvaServices proveedorEvaService;
    @Autowired
    private IProveedorServices proveedorService;
    private final EmailService emailService;

    public AutenticacionRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/autenticacion")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerAutenticacion(){
        List<Autenticacion> autenticacionDb = autenticacionService.encontrarAutenticacion("PA");
        if(autenticacionDb!=null){
            return  new ResponseEntity<>(autenticacionDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/autenticacion")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder autCodigoCorreo = new StringBuilder();

        switch (opcion){
            case "cantidad":
                Integer cantidad = autenticacionService.cantidadAutenticacion(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    String autCorreo = jsonNode.get("aut_correo").asText().trim();
                    String tdcTd = jsonNode.get("tdc_td").asText().trim();
                    String prvNd = jsonNode.get("prv_nd").asText().trim();

                    Usuario miUs = usuarioService.encontrarUsuariosPorNombre(autCorreo);
                    if (miUs != null) return new ResponseEntity<>("correo_ya_registrado", HttpStatus.OK);
                    if (proveedorService.encontrarProveedoresPorNdyTdcTd(prvNd,tdcTd) != null) return new ResponseEntity<>("proveedor_ya_existe", HttpStatus.OK);

                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String autContrasena = jsonNode.get("aut_contrasena").asText().trim();
                    String prvNombre = jsonNode.get("prv_nombre").asText().trim();
                    Integer proId = jsonNode.get("pro_id").asInt();
                    Integer sprId = jsonNode.get("spr_id").asInt();
                    String prvCelular = jsonNode.get("prv_celular").asText().trim();
                    String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
                    String paiNombre = jsonNode.get("pai_nombre").asText().trim();
                    String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
                    String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();


                    if (!regexService.isId(idEmppal) || !regexService.isMail(autCorreo) || !regexService.isTextNormal(tdcTd) ||
                            !regexService.isTextNormal(prvNd) || !regexService.isTextNormal(prvNombre) || !regexService.isSelectNumber(proId) || !regexService.isSelectNumber(sprId) ||
                            !regexService.isTextNormal(prvCelular) ||!regexService.isTextNormal(prvDireccion) ||!regexService.isTextNormal(paiNombre) ||!regexService.isTextNormal(dptNombre) ||
                            !regexService.isTextNormal(ciuNombre) || !regexService.isPassword(autContrasena)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encodedPassword = passwordEncoder.encode(autContrasena);

                    Autenticacion autenticacionDb = autenticacionService.encontrarAutenticacionPorNombre(autCorreo.toLowerCase());
                    if(autenticacionDb == null) {
                       Autenticacion miAutenticacion = new Autenticacion();
                       miAutenticacion.setIdEmppal(idEmppal);

                        for (int i = 0; i < 6; i++) {
                            int indice = random.nextInt(caracteres.length());
                            autCodigoCorreo.append(caracteres.charAt(indice));
                        }

                       miAutenticacion.setAutCorreo(autCorreo.toLowerCase());
                       miAutenticacion.setTdcTd(tdcTd.toLowerCase());
                       miAutenticacion.setPrvNd(prvNd.toLowerCase());
                       miAutenticacion.setPrvNombre(prvNombre.toLowerCase());
                       miAutenticacion.setProId(proId);
                       miAutenticacion.setSprId(sprId);
                       miAutenticacion.setAutCodigoCorreo(String.valueOf(autCodigoCorreo));
                       miAutenticacion.setPrvCelular(prvCelular.toLowerCase());
                       miAutenticacion.setPrvDireccion(prvDireccion.toLowerCase());
                       miAutenticacion.setPaiNombre(paiNombre.toLowerCase());
                       miAutenticacion.setDptNombre(dptNombre.toLowerCase());
                       miAutenticacion.setCiuNombre(ciuNombre.toLowerCase());
                       miAutenticacion.setAutContrasena(encodedPassword);
                       miAutenticacion.setAutEstado("E");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miAutenticacion.setAudFecha(fechaActual);
                       miAutenticacion.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            autenticacionService.crearAutenticacion(miAutenticacion);
                            emailService.sendListEmailRegistro(autCorreo.toLowerCase(), prvNombre.toLowerCase(), String.valueOf(autCodigoCorreo) );

                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>("se_crea_solicitud", HttpStatus.OK);
                    }else{
                        autCodigoCorreo = new StringBuilder("");
                        for (int i = 0; i < 6; i++) {
                            int indice = random.nextInt(caracteres.length());
                            autCodigoCorreo.append(caracteres.charAt(indice));
                        }
                        emailService.sendListEmailRegistro(autCorreo.toLowerCase(), prvNombre.toLowerCase(), String.valueOf(autCodigoCorreo) );
                        autenticacionDb.setAutCodigoCorreo(String.valueOf(autCodigoCorreo));
                        autenticacionDb.setAutEstado("E");
                        autenticacionDb.setAutCorreo(autCorreo.toLowerCase());
                        autenticacionDb.setTdcTd(tdcTd.toLowerCase());
                        autenticacionDb.setPrvNd(prvNd.toLowerCase());
                        autenticacionDb.setPrvNombre(prvNombre.toLowerCase());
                        autenticacionDb.setProId(proId);
                        autenticacionDb.setSprId(sprId);
                        autenticacionDb.setAutCodigoCorreo(String.valueOf(autCodigoCorreo));
                        autenticacionDb.setPrvCelular(prvCelular.toLowerCase());
                        autenticacionDb.setPrvDireccion(prvDireccion.toLowerCase());
                        autenticacionDb.setPaiNombre(paiNombre.toLowerCase());
                        autenticacionDb.setDptNombre(dptNombre.toLowerCase());
                        autenticacionDb.setCiuNombre(ciuNombre.toLowerCase());
                        autenticacionDb.setAutContrasena(encodedPassword);

                        autenticacionService.actualizarAutenticacion(autenticacionDb);
                        return  new ResponseEntity<>("codigo_correo_actualizado" , HttpStatus.OK);
                    }
                }catch (Exception e){
                    if (e.getMessage().contains("null")){
                        System.out.println(e.getMessage());
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

                List<Autenticacion> autenticacionTodosDb = autenticacionService.encontrarAutenticacionFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(autenticacionTodosDb!=null){
                    return  new ResponseEntity<>(autenticacionTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer autenticacionTodosDbC = autenticacionService.cantidadPaginasAutenticacion(checkBoxEstado,textoC);
                if(autenticacionTodosDbC!=null){
                    return  new ResponseEntity<>(autenticacionTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer autId = (Integer) requestBody.get("aut_id");
                Autenticacion autenticacionDbI = autenticacionService.encontrarAutenticacionPorId(autId,null);
                if(autenticacionDbI!=null){
                    return  new ResponseEntity<>(autenticacionDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer autIdD = (Integer) requestBody.get("aut_id");
                Autenticacion autenticacionDbB =   autenticacionService.encontrarAutenticacionPorId(autIdD,"PA");

                if(autenticacionDbB != null) {
                    autenticacionDbB.setAutEstado("I");
                    autenticacionService.actualizarAutenticacion(autenticacionDbB);
                    return  new ResponseEntity<>(autenticacionDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer autIdDA = (Integer) requestBody.get("aut_id");
                Autenticacion autenticacionDbBA =   autenticacionService.encontrarAutenticacionPorId(autIdDA,"I");
                if(autenticacionDbBA != null) {
                    autenticacionDbBA.setAutEstado("PA");
                    autenticacionService.actualizarAutenticacion(autenticacionDbBA);
                    return  new ResponseEntity<>(autenticacionDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "autenticacionSoloNombre":
                List<Autenticacion> autenticacionDbS = autenticacionService.encontrarAutenticacionNombres("PA");
                if(autenticacionDbS!=null && !autenticacionDbS.isEmpty()){
                    return  new ResponseEntity<>(autenticacionDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "verificarCodigo":
                String codigoCorreo = (String) requestBody.get("aut_codigoCorreo");
                if (!regexService.isTextNormal(codigoCorreo)
                )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                if (codigoCorreo.length() <6 )return ResponseEntity.ok("caracteres_no_corresponden");
                Autenticacion miAutenticacion = autenticacionService.encontrarAutenticacionPorCodigo(codigoCorreo);
                if (miAutenticacion == null) {
                    return ResponseEntity.ok("codigo_errado");
                } else {
                    miAutenticacion.setAutEstado("PA");
                    miAutenticacion.setAutCodigoCorreo("");
                    autenticacionService.actualizarAutenticacion(miAutenticacion);
                    emailService.sendListEmailConfirmacion(miAutenticacion.getAutCorreo(), miAutenticacion.getPrvNombre());
                    return ResponseEntity.ok("verificado");
                }

            case "correoRecuperarContrasena":
                String correoR = (String) requestBody.get("aut_correo");
                if (!regexService.isMail(correoR)
                )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                Usuario miUsuario = usuarioService.encontrarUsuariosPorNombre(correoR);
                Autenticacion miAut = autenticacionService.encontrarAutenticacionPorNombre(correoR);

                if (miUsuario == null) return ResponseEntity.ok("correo_no_registra");
                for (int i = 0; i < 6; i++) {
                    int indice = random.nextInt(caracteres.length());
                    autCodigoCorreo.append(caracteres.charAt(indice));
                }
                emailService.sendCorreoRecuperar(correoR, String.valueOf(autCodigoCorreo));
                miAut.setAutCodigoCorreo(String.valueOf(autCodigoCorreo));
                autenticacionService.actualizarAutenticacion(miAut);
                return ResponseEntity.ok("enviado");

            case "cambiarContrasena":
                String usuContrasena = (String) requestBody.get("usu_contrasena");
                String codigo = (String) requestBody.get("codigo");
                if (!regexService.isPassword(usuContrasena)) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);
                Autenticacion miAutR = autenticacionService.encontrarAutenticacionPorCodigo(codigo);
                if (miAutR == null) return ResponseEntity.ok("sin_codigo");
                Usuario miUsuR = usuarioService.encontrarUsuariosPorNombre(miAutR.getAutCorreo());
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(usuContrasena);
                miUsuR.setUsuContrasena(encodedPassword);
                miAutR.setAutCodigoCorreo("");
                autenticacionService.actualizarAutenticacion(miAutR);
                return ResponseEntity.ok("correcto");
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }
    }

    @PutMapping("/autenticacion")
    public ResponseEntity<?> actualizarAutenticacion(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String autenticacion = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(autenticacion);
        try {
            String autCorreo = jsonNode.get("aut_correo").asText().trim();
            String tdcTd = jsonNode.get("tdc_td").asText().trim();
            String prvNd = jsonNode.get("prv_nd").asText().trim();
            String prvNombre = jsonNode.get("prv_nombre").asText().trim();
            Integer proId = jsonNode.get("pro_id").asInt();
            Integer sprId = jsonNode.get("spr_id").asInt();
            String prvCelular = jsonNode.get("prv_celular").asText().trim();
            String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
            String paiNombre = jsonNode.get("pai_nombre").asText().trim();
            String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
            String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
            Integer perId = jsonNode.get("per_id").asInt();
            Integer crtId = jsonNode.get("crt_id").asInt();

            Usuario miUs = usuarioService.encontrarUsuariosPorNombre(autCorreo);
            if (miUs != null) return new ResponseEntity<>("correo_ya_registrado", HttpStatus.OK);
            if (proveedorService.encontrarProveedoresPorNdyTdcTd(prvNd,tdcTd) != null) return new ResponseEntity<>("proveedor_ya_existe", HttpStatus.OK);


            if (!regexService.isMail(autCorreo) || !regexService.isTextNormal(tdcTd) ||
                    !regexService.isTextNormal(prvNd) || !regexService.isTextNormal(prvNombre) || !regexService.isSelectNumber(proId) || !regexService.isSelectNumber(sprId) ||
                    !regexService.isTextNormal(prvCelular) ||!regexService.isTextNormal(prvDireccion) ||!regexService.isTextNormal(paiNombre) ||!regexService.isTextNormal(dptNombre) ||
                    !regexService.isTextNormal(ciuNombre)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            Autenticacion autenticacionDb = autenticacionService.encontrarAutenticacionPorNombre(autCorreo.toLowerCase());

            if (autenticacionDb != null) {
                autenticacionDb.setAutCorreo(autCorreo.toLowerCase());
                autenticacionDb.setTdcTd(tdcTd.toLowerCase());
                autenticacionDb.setPrvNd(prvNd.toLowerCase());
                autenticacionDb.setPrvNombre(prvNombre.toLowerCase());
                autenticacionDb.setProId(proId);
                autenticacionDb.setSprId(sprId);
                autenticacionDb.setPrvCelular(prvCelular.toLowerCase());
                autenticacionDb.setPrvDireccion(prvDireccion.toLowerCase());
                autenticacionDb.setPaiNombre(paiNombre.toLowerCase());
                autenticacionDb.setDptNombre(dptNombre.toLowerCase());
                autenticacionDb.setCiuNombre(ciuNombre.toLowerCase());
                autenticacionDb.setAutEstado("A");
                try {
                    autenticacionService.actualizarAutenticacion(autenticacionDb);
                    Usuario miUsuario = new Usuario();
                    miUsuario.setIdEmppal(autenticacionDb.getIdEmppal());
                    miUsuario.setUsuNombre(autenticacionDb.getPrvNombre());
                    miUsuario.setUsuDocumento(autenticacionDb.getPrvNombre());
                    miUsuario.setUsuCorreo(autenticacionDb.getAutCorreo());
                    miUsuario.setUsuContrasena(autenticacionDb.getAutContrasena());
                    miUsuario.setUsuTipo("proveedor");
                    miUsuario.setUsuRol("proveedor");
                    miUsuario.setUsuEstado("A");
                    Date fechaActual = new Date();
                    Set<Date> conjuntoFechas = new HashSet<>();
                    conjuntoFechas.add(fechaActual);
                    miUsuario.setAudFecha(fechaActual);
                    miUsuario.setAudUsuario(autenticacionDb.getAudUsuario());
                    try {
                        usuarioService.crearUsuario(miUsuario);
                        emailService.sendListEmailIngresoCorrecto(autCorreo.toLowerCase(), prvNombre.toLowerCase());

                        Proveedor miProveedor = new Proveedor();
                        miProveedor.setIdEmppal(autenticacionDb.getIdEmppal());
                        miProveedor.setPrvNd(prvNd);
                        miProveedor.setTdcTd(tdcTd.toLowerCase());
                        miProveedor.setPrvNombre(prvNombre.toLowerCase());
                        miProveedor.setProId(proId);
                        miProveedor.setSprId(sprId);
                        miProveedor.setPrvCelular(prvCelular);
                        miProveedor.setPrvDireccion(prvDireccion);
                        miProveedor.setPaiNombre(paiNombre.toLowerCase());
                        miProveedor.setDptNombre(dptNombre.toLowerCase());
                        miProveedor.setCiuNombre(ciuNombre.toLowerCase());
                        miProveedor.setPrvCorreo(autCorreo.toLowerCase());
                        miProveedor.setCrtId(crtId);
                        miProveedor.setPrvEstado("A");
                        Date fechaActualN = new Date();
                        Set<Date> conjuntoFechasN = new HashSet<>();
                        conjuntoFechasN.add(fechaActualN);
                        miProveedor.setAudFecha(fechaActualN);
                        miProveedor.setAudUsuario(autenticacionDb.getAudUsuario());
                        try {
                            proveedorService.crearProveedor(miProveedor);
                            ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(perId, miProveedor.getPrvId());
                            if(proveedorEvasDb == null) {
                                ProveedorEva miProveedorEva = new ProveedorEva();
                                miProveedorEva.setIdEmppal(miProveedor.getIdEmppal());
                                miProveedorEva.setPerId(perId);
                                miProveedorEva.setPrvId(miProveedor.getPrvId());
                                miProveedorEva.setPreResultado(0);
                                miProveedorEva.setPreObservacion("");
                                miProveedorEva.setPreContinua("si");
                                miProveedorEva.setPreEstado("NI");
                                miProveedorEva.setAudFecha(fechaActualN);
                                miProveedorEva.setAudUsuario(autenticacionDb.getAudUsuario());
                                proveedorEvaService.crearProveedorEva(miProveedorEva);
                            }
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                    } catch (DataIntegrityViolationException e) {
                        if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                            String data = "dato_existente";
                            return new ResponseEntity<>(data, HttpStatus.OK);
                        } else {
                            String data = "error_sql";
                            return new ResponseEntity<>( data, HttpStatus.OK);
                        }
                    }
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
