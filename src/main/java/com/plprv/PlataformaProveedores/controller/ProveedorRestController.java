package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import com.plprv.PlataformaProveedores.entity.Usuario;
import com.plprv.PlataformaProveedores.service.IProveedorEvaServices;
import com.plprv.PlataformaProveedores.service.IProveedorServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.IUsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IProveedorEvaServices proveedorEvaService;
    @GetMapping("/proveedor")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerProveedores(){
        List<Proveedor> proveedoresDb = proveedorService.encontrarProveedores("A");
        if(proveedoresDb!=null){
            return  new ResponseEntity<>(proveedoresDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/proveedor")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");


        switch (opcion){
            case "cantidad":
                Integer cantidad = proveedorService.cantidadProveedores(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    Random random = new Random();
                    StringBuilder autCodigoCorreo = new StringBuilder();

                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String prvNd = jsonNode.get("prv_nd").asText().trim();
                    String tdcTd = jsonNode.get("tdc_td").asText().trim();
                    String prvNombre = jsonNode.get("prv_nombre").asText().trim();
                    Integer proId = jsonNode.get("pro_id").asInt();
                    Integer sprId = jsonNode.get("spr_id").asInt();
                    String prvCelular = jsonNode.get("prv_celular").asText().trim();
                    String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
                    String paiNombre = jsonNode.get("pai_nombre").asText().trim();
                    String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
                    String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
                    String prvCorreo = jsonNode.get("prv_correo").asText().trim();
                    Integer perId = jsonNode.get("per_id").asInt();
                    Integer crtId = jsonNode.get("crt_id").asInt();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isMail(prvCorreo) || !regexService.isTextNormal(tdcTd) ||
                            !regexService.isTextNormal(prvNd) || !regexService.isTextNormal(prvNombre) || !regexService.isSelectNumber(proId) || !regexService.isSelectNumber(sprId) ||
                            !regexService.isTextNormal(prvCelular) ||!regexService.isTextNormal(prvDireccion) ||!regexService.isTextNormal(paiNombre) ||!regexService.isTextNormal(dptNombre) ||
                            !regexService.isTextNormal(ciuNombre)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    Usuario miUs = usuarioService.encontrarUsuariosPorNombre(prvCorreo);
                    if (miUs != null) return new ResponseEntity<>("correo_ya_registrado", HttpStatus.OK);
                    if (proveedorService.encontrarProveedoresPorNdyTdcTd(prvNd,tdcTd) != null) return new ResponseEntity<>("proveedor_ya_existe", HttpStatus.OK);

                    for (int i = 0; i < 6; i++) {
                        int indice = random.nextInt(caracteres.length());
                        autCodigoCorreo.append(caracteres.charAt(indice));
                    }

                    Proveedor proveedoresDb = proveedorService.encontrarProveedoresPorNombre(prvNombre.toLowerCase());
                    if(proveedoresDb == null) {
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
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miProveedor.setAudFecha(fechaActual);
                       miProveedor.setAudUsuario(audUsuario.toLowerCase());
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
                            conjuntoFechas.add(fechaActual);
                            miUsuario.setAudFecha(fechaActual);
                            miUsuario.setAudUsuario(miProveedor.getAudUsuario());
                            try {
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
                                    miProveedorEva.setAudFecha(fechaActual);
                                    miProveedorEva.setAudUsuario(miProveedor.getAudUsuario());
                                    proveedorEvaService.crearProveedorEva(miProveedorEva);
                                }
                                usuarioService.crearUsuario(miUsuario);
                            } catch (Exception e) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
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
                       return new ResponseEntity<>(miProveedor, HttpStatus.OK);
                    }else{
                        proveedoresDb.setPrvEstado("A");
                        proveedorService.actualizarProveedor(proveedoresDb);
                        return  new ResponseEntity<>(proveedoresDb , HttpStatus.OK);
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

                List<Proveedor> proveedoresTodosDb = proveedorService.encontrarProveedoresFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(proveedoresTodosDb!=null){
                    return  new ResponseEntity<>(proveedoresTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }

            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer proveedorTodosDbC = proveedorService.cantidadPaginasProveedores(checkBoxEstado,textoC);
                if(proveedorTodosDbC!=null){
                    return  new ResponseEntity<>(proveedorTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer prv_id = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbI = proveedorService.encontrarProveedoresPorId(prv_id,"A");
                if(proveedoresDbI!=null){
                    return  new ResponseEntity<>(proveedoresDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer prvIdD = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbB =   proveedorService.encontrarProveedoresPorId(prvIdD,"A");

                if(proveedoresDbB != null) {
                    proveedoresDbB.setPrvEstado("I");
                    proveedorService.actualizarProveedor(proveedoresDbB);
                    Usuario usuariosDbB =   usuarioService.encontrarUsuariosPorNombre(proveedoresDbB.getPrvCorreo());
                    if(usuariosDbB != null) {
                        usuariosDbB.setUsuEstado("I");
                        usuarioService.actualizarUsuario(usuariosDbB);
                        return  new ResponseEntity<>(proveedoresDbB , HttpStatus.OK);
                    }else {
                        return new ResponseEntity<>(null,HttpStatus.OK);
                    }
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }


            case "activar":
                Integer prvIdDA = (Integer) requestBody.get("prv_id");
                Proveedor proveedoresDbBA =   proveedorService.encontrarProveedoresPorId(prvIdDA,"I");

                if(proveedoresDbBA != null) {
                    proveedoresDbBA.setPrvEstado("A");
                    proveedorService.actualizarProveedor(proveedoresDbBA);
                    Usuario usuariosDbB =   usuarioService.encontrarUsuariosPorNombre(proveedoresDbBA.getPrvCorreo());
                    if(usuariosDbB != null) {
                        usuariosDbB.setUsuEstado("A");
                        usuarioService.actualizarUsuario(usuariosDbB);
                        return  new ResponseEntity<>(proveedoresDbBA , HttpStatus.OK);
                    }else {
                        return new ResponseEntity<>(null,HttpStatus.OK);
                    }
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }


            case "proveedorSoloNombre":
                String textoD = (String) requestBody.get("texto");
                List<Proveedor> proveedoresDbS = proveedorService.encontrarProveedoresNombres("A",textoD);
                if(proveedoresDbS!=null){
                    return  new ResponseEntity<>(proveedoresDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }

            case "proveedorPorToken":
                Integer prvId = (Integer) requestBody.get("prv_id");
                if(prvId == 0) return ResponseEntity.ok("Id no encontrada");
                Proveedor proveedoresDbIT = proveedorService.encontrarProveedoresSoloPorId(prvId);
                if(proveedoresDbIT!=null){
                    return  new ResponseEntity<>(proveedoresDbIT , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/proveedor")
    public ResponseEntity<?> actualizarProveedor(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String proveedor = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(proveedor);
        try {
            Integer prvId = jsonNode.get("prv_id").asInt();
            String prvNd = jsonNode.get("prv_nd").asText().trim();
            String tdcTd = jsonNode.get("tdc_td").asText().trim();
            String prvNombre = jsonNode.get("prv_nombre").asText().trim();
            Integer proId = jsonNode.get("pro_id").asInt();
            Integer sprId = jsonNode.get("spr_id").asInt();
            String prvCelular = jsonNode.get("prv_celular").asText().trim();
            String prvDireccion = jsonNode.get("prv_direccion").asText().trim();
            String paiNombre = jsonNode.get("pai_nombre").asText().trim();
            String dptNombre = jsonNode.get("dpt_nombre").asText().trim();
            String ciuNombre = jsonNode.get("ciu_nombre").asText().trim();
            //String prvCorreo = jsonNode.get("prv_correo").asText().trim();

            Proveedor proveedorDb = proveedorService.encontrarProveedoresPorId(prvId, "A");

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
