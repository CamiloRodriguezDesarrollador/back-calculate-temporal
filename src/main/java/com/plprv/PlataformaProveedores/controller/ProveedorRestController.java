package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.*;
import com.plprv.PlataformaProveedores.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProveedorRestController {

    @Autowired
    private IUsuarioServices usuarioService;
    @Autowired
    private IProveedorServices proveedorService;
    @Autowired
    private IRegexService regexService;

    private final EmailService emailService;

    @Autowired
    private IProveedorEvaServices proveedorEvaService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    public ProveedorRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/proveedor")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
    public ResponseEntity<?> obtenerProveedores(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<Proveedor> proveedoresDb = proveedorService.encontrarProveedores("A",miIdEmppal);
        if(proveedoresDb!=null){
            return  new ResponseEntity<>(proveedoresDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/proveedor")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder autCodigoCorreo = new StringBuilder();

        switch (opcion) {
            case "cantidad" -> {
                Integer cantidad = proveedorService.cantidadProveedores(checkBoxEstado,miIdEmppal);
                return ResponseEntity.ok(cantidad);
            }
            case "crear" -> {
                try {

                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String prvNd = jsonNode.get("prv_nd").asText().trim();
                    String tdcTd = jsonNode.get("tdc_td").asText().trim();
                    String prvNombre = jsonNode.get("prv_nombre").asText().trim();
                    int proId = jsonNode.get("pro_id").asInt();
                    int sprId = jsonNode.get("spr_id").asInt();
                    String prvCelular = jsonNode.get("prv_celular").asText().trim();
                    String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
                    String paiNombre = jsonNode.get("pai_nombre").asText().trim();
                    String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
                    String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
                    String prvCorreo = jsonNode.get("prv_correo").asText().trim();
                    int perId = jsonNode.get("per_id").asInt();
                    int crtId = jsonNode.get("crt_id").asInt();

                    if (!regexService.isId(idEmppal) || !regexService.isMail(prvCorreo) || !regexService.isTextNormal(tdcTd) ||
                            !regexService.isTextNormal(prvNd) || !regexService.isTextNormal(prvNombre) || !regexService.isSelectNumber(proId) || !regexService.isSelectNumber(sprId) ||
                            !regexService.isTextNormal(prvCelular) || !regexService.isTextNormal(prvDireccion) || !regexService.isTextNormal(paiNombre) || !regexService.isTextNormal(dptNombre) ||
                            !regexService.isTextNormal(ciuNombre)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    Usuario miUs = usuarioService.encontrarUsuariosPorNombre(prvCorreo,miIdEmppal);
                    if (miUs != null) return new ResponseEntity<>("correo_ya_registrado", HttpStatus.OK);
                    if (proveedorService.encontrarProveedoresPorNdyTdcTd(prvNd, tdcTd,miIdEmppal) != null)
                        return new ResponseEntity<>("proveedor_ya_existe", HttpStatus.OK);

                    for (int i = 0; i < 25; i++) {
                        int indice = random.nextInt(caracteres.length());
                        autCodigoCorreo.append(caracteres.charAt(indice));
                    }

                    Proveedor proveedoresDb = proveedorService.encontrarProveedoresPorNombre(prvNombre.toLowerCase(),miIdEmppal);
                    if (proveedoresDb == null) {
                        Proveedor miProveedor = new Proveedor();
                        miProveedor.setIdEmppal(idEmppal);
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
                        miProveedor.setPrvCorreo(prvCorreo.toLowerCase());
                        miProveedor.setCrtId(crtId);
                        miProveedor.setPrvEstado("A");
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miProveedor.setAudFecha(fechaActual);
                        miProveedor.setAudUsuario(miAud);
                        try {
                            proveedorService.crearProveedor(miProveedor);
                            Usuario miUsuario = new Usuario();
                            miUsuario.setIdEmppal(miProveedor.getIdEmppal());
                            miUsuario.setUsuNombre(miProveedor.getPrvNombre());
                            miUsuario.setUsuDocumento(miProveedor.getPrvNombre());
                            miUsuario.setUsuCorreo(miProveedor.getPrvCorreo());
                            miUsuario.setUsuContrasena(String.valueOf(autCodigoCorreo));
                            miUsuario.setUsuTipo("proveedor");
                            miUsuario.setUsuRol("proveedor");
                            miUsuario.setUsuEstado("A");
                            conjunctrecast.add(fechaActual);
                            miUsuario.setAudFecha(fechaActual);
                            miUsuario.setAudUsuario(miAud);
                            try {
                                ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(perId, miProveedor.getPrvId(),miIdEmppal);
                                if (proveedorEvasDb == null) {
                                    ProveedorEva miProveedorEva = new ProveedorEva();
                                    miProveedorEva.setIdEmppal(miProveedor.getIdEmppal());
                                    miProveedorEva.setPerId(perId);
                                    miProveedorEva.setPrvId(miProveedor.getPrvId());
                                    miProveedorEva.setPreResultado(0);
                                    miProveedorEva.setPreObservacion("");
                                    miProveedorEva.setPreContinua("si");
                                    miProveedorEva.setPreEstado("NI");
                                    miProveedorEva.setAudFecha(fechaActual);
                                    miProveedorEva.setAudUsuario(miAud);
                                    proveedorEvaService.crearProveedorEva(miProveedorEva);
                                }
                                usuarioService.crearUsuario(miUsuario);
                            } catch (Exception e) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miProveedor, HttpStatus.OK);
                    } else {
                        proveedoresDb.setPrvEstado("A");
                        proveedorService.actualizarProveedor(proveedoresDb);
                        return new ResponseEntity<>(proveedoresDb, HttpStatus.OK);
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
            case "informacionTotal" -> {
                Integer numeroDePagina = (Integer) requestBody.get("numeroDePagina");
                Integer numeroElementosPorPagina = (Integer) requestBody.get("numeroElementosPorPagina");
                String texto = (String) requestBody.get("texto");
                List<Proveedor> proveedoresTodosDb = proveedorService.encontrarProveedoresFiltroPaginas(checkBoxEstado, texto, numeroDePagina,
                        numeroElementosPorPagina,miIdEmppal);
                if (proveedoresTodosDb != null) {
                    return new ResponseEntity<>(proveedoresTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" -> {
                String textoC = (String) requestBody.get("texto");
                Integer proveedorTodosDbC = proveedorService.cantidadPaginasProveedores(checkBoxEstado, textoC,miIdEmppal);
                if (proveedorTodosDbC != null) {
                    return new ResponseEntity<>(proveedorTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" -> {
                Integer prv_id = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbI = proveedorService.encontrarProveedoresPorId(prv_id, "A",miIdEmppal);
                if (proveedoresDbI != null) {
                    return new ResponseEntity<>(proveedoresDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "borrar" -> {
                Integer prvIdD = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbB = proveedorService.encontrarProveedoresPorId(prvIdD, "A",miIdEmppal);
                if (proveedoresDbB != null) {
                    proveedoresDbB.setPrvEstado("I");
                    proveedorService.actualizarProveedor(proveedoresDbB);
                    Usuario usuariosDbB = usuarioService.encontrarUsuariosPorNombre(proveedoresDbB.getPrvCorreo(),miIdEmppal);
                    if (usuariosDbB != null) {
                        usuariosDbB.setUsuEstado("I");
                        usuarioService.actualizarUsuario(usuariosDbB);
                        return new ResponseEntity<>(proveedoresDbB, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "activar" -> {
                Integer prvIdDA = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbBA = proveedorService.encontrarProveedoresPorId(prvIdDA, "I",miIdEmppal);
                if (proveedoresDbBA != null) {
                    proveedoresDbBA.setPrvEstado("A");
                    proveedorService.actualizarProveedor(proveedoresDbBA);
                    Usuario usuariosDbB = usuarioService.encontrarUsuariosPorNombre(proveedoresDbBA.getPrvCorreo(),miIdEmppal);
                    if (usuariosDbB != null) {
                        usuariosDbB.setUsuEstado("A");
                        usuarioService.actualizarUsuario(usuariosDbB);
                        return new ResponseEntity<>(proveedoresDbBA, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "proveedorSoloNombre" -> {
                String textoD = (String) requestBody.get("texto");
                List<Proveedor> proveedoresDbS = proveedorService.encontrarProveedoresNombres("A", textoD,miIdEmppal);
                if (proveedoresDbS != null) {
                    return new ResponseEntity<>(proveedoresDbS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "informacionSistema" -> {
                Integer nitNd = (Integer) requestBody.get("nit_nd");
                String tdcTd = (String) requestBody.get("tdc_td");
                if (tdcTd.equals("persona natural")) tdcTd = "CC";
                if (tdcTd.equals("persona juridica")) tdcTd = "NI";

                try{
                    Object miProveedorSistema = proveedorService.encontrarProveedorSistema(nitNd,tdcTd);
                    if (miProveedorSistema != null) {
                        return new ResponseEntity<>(miProveedorSistema, HttpStatus.OK);
                    } else {

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }


            }
            default -> {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }
    }

    @PostMapping("/proveedorL")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
        String opcion = (String) requestBody.get("opcion");
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder autCodigoCorreo = new StringBuilder();
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);

        switch (opcion) {
            case "proveedorPorToken" -> {

                Integer prvId = (Integer) requestBody.get("prv_id");
                if (prvId == 0) return ResponseEntity.ok("Id no encontrada");
                Proveedor proveedoresDbIT = proveedorService.encontrarProveedoresSoloPorId(prvId,miIdEmppal);
                if (proveedoresDbIT != null) {

                    return new ResponseEntity<>(proveedoresDbIT, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "proveedorPorTokenGenerada" -> {
                String tokenM = (String) requestBody.get("token");
                Proveedor proveedoresDbITo = proveedorService.encontrarProveedoresPorToken(tokenM, "A",miIdEmppal);
                if (proveedoresDbITo != null) {
                    return new ResponseEntity<>(proveedoresDbITo, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "editarContrasena" -> {
                String contrasenaAntigua = (String) requestBody.get("prv_contrasena");
                contrasenaAntigua = unwrapPassword(contrasenaAntigua);
                String contrasenaNueva = (String) requestBody.get("prv_contrasenaN");
                contrasenaNueva = unwrapPassword(contrasenaNueva);
                if (!regexService.isPassword(contrasenaAntigua) || !regexService.isPassword(contrasenaNueva)
                ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);
                Integer prvIdC = (Integer) requestBody.get("prv_id");
                if (prvIdC == null) return new ResponseEntity<>("sin_proveedor", HttpStatus.OK);
                Proveedor proveedorContrasena = proveedorService.encontrarProveedoresPorId(prvIdC, "A",miIdEmppal);
                String correo = proveedorContrasena.getPrvCorreo();
                Usuario usuarioContrasena = usuarioService.encontrarUsuariosPorNombre(correo,miIdEmppal);
                if (usuarioContrasena == null) return new ResponseEntity<>("sin_usuario", HttpStatus.OK);
                String contrasena = usuarioContrasena.getUsuContrasena();
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPasswordN = passwordEncoder.encode(contrasenaNueva);
                if (passwordEncoder.matches(contrasenaAntigua, contrasena)) {
                    usuarioContrasena.setUsuContrasena(encodedPasswordN);
                    usuarioService.actualizarUsuario(usuarioContrasena);
                    return new ResponseEntity<>("modificado", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("contrasena_no_conincide", HttpStatus.OK);
                }
            }
            case "cambiarCorreo" -> {
                String usuCorreoNuevo = (String) requestBody.get("usu_correo");
                Integer prvIdCo = (Integer) requestBody.get("prv_id");
                if (!regexService.isMail(usuCorreoNuevo)
                ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);
                Proveedor miProveedorUsu = proveedorService.encontrarProveedorPorCorreo(usuCorreoNuevo,miIdEmppal);
                if (miProveedorUsu != null) return new ResponseEntity<>("correo_ya_asignado", HttpStatus.OK);
                Proveedor miProveedorId = proveedorService.encontrarProveedoresPorId(prvIdCo, "A",miIdEmppal);
                if (miProveedorId == null) return new ResponseEntity<>("no_registra", HttpStatus.OK);
                for (int i = 0; i < 6; i++) {
                    int indice = random.nextInt(caracteres.length());
                    autCodigoCorreo.append(caracteres.charAt(indice));
                }
                miProveedorId.setPrvToken(String.valueOf(autCodigoCorreo));
                proveedorService.actualizarProveedor(miProveedorId);
                emailService.sendListEmailCambioCorreo(usuCorreoNuevo, String.valueOf(autCodigoCorreo));
                return new ResponseEntity<>("modificado", HttpStatus.OK);
            }
            case "verificarCorreoCambio" -> {
                String codigoCorreo = (String) requestBody.get("codigoCorreo");
                String usuCorreo = (String) requestBody.get("usu_correo");
                if (!regexService.isTextNormal(codigoCorreo) || !regexService.isMail(usuCorreo)
                ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);
                Proveedor miProvedorCorreo = proveedorService.encontrarProveedorPorToken(codigoCorreo,miIdEmppal);
                if (miProvedorCorreo == null) return new ResponseEntity<>("codigo_errado", HttpStatus.OK);
                Usuario miUsuarioCorreo = usuarioService.encontrarUsuariosPorNombre(miProvedorCorreo.getPrvCorreo(),miIdEmppal);
                miProvedorCorreo.setPrvCorreo(usuCorreo);
                proveedorService.actualizarProveedor(miProvedorCorreo);
                if (miUsuarioCorreo == null) return new ResponseEntity<>("usuario_no_existe", HttpStatus.OK);
                miUsuarioCorreo.setUsuCorreo(usuCorreo);
                usuarioService.actualizarUsuario(miUsuarioCorreo);
                return new ResponseEntity<>("modificado", HttpStatus.OK);
            }
            default -> {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }
    }

    @PutMapping("/proveedor")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','administrador')")
    public ResponseEntity<?> actualizarProveedor(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String proveedor = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(proveedor);
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        try {
            int prvId = jsonNode.get("prv_id").asInt();
            String prvNd = jsonNode.get("prv_nd").asText().trim();
            String tdcTd = jsonNode.get("tdc_td").asText().trim();
            String prvNombre = jsonNode.get("prv_nombre").asText().trim();
            int proId = jsonNode.get("pro_id").asInt();
            int sprId = jsonNode.get("spr_id").asInt();
            String prvCelular = jsonNode.get("prv_celular").asText().trim();
            String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
            String paiNombre = jsonNode.get("pai_nombre").asText().trim();
            String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
            String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
            //String prvCorreo = jsonNode.get("prv_correo").asText().trim();
            Proveedor proveedorDb = proveedorService.encontrarProveedoresPorId(prvId, "A",miIdEmppal);
            if (proveedorDb != null) {

                if (proId != 0) proveedorDb.setProId(proId);
                if (sprId != 0) proveedorDb.setSprId(sprId);
                if (!prvNd.equals("")) proveedorDb.setPrvNd(prvNd);
                if (!tdcTd.equals("")) proveedorDb.setTdcTd(tdcTd);
                if (!prvNombre.equals("")) proveedorDb.setPrvNombre(prvNombre);

                proveedorDb.setPrvCelular(prvCelular);
                proveedorDb.setPrvDireccion(prvDireccion);
                proveedorDb.setPaiNombre(paiNombre);
                proveedorDb.setDptNombre(dptNombre);
                proveedorDb.setCiuNombre(ciuNombre);
                //proveedorDb.setPrvCorreo(prvCorreo);
                try {
                    proveedorService.actualizarProveedor(proveedorDb);
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
