package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Criticidad;
import com.plprv.PlataformaProveedores.service.ICriticidadServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ObtenerUsuarioAud;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CriticidadRestController {
    @Autowired
    private ICriticidadServices criticidadService;
    @Autowired
    private IRegexService regexService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    @GetMapping("/criticidad")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> obtenerCriticidads(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<Criticidad> criticidadsDb = criticidadService.encontrarCriticidads("A",miIdEmppal);
        if(criticidadsDb!=null){
            return  new ResponseEntity<>(criticidadsDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/criticidad")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
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
                Integer cantidad = criticidadService.cantidadCriticidads(checkBoxEstado,miIdEmppal);
                return ResponseEntity.ok(cantidad);
            }
            case "crear" : {
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String crtNombre = jsonNode.get("crt_nombre").asText().trim();
                    String crtDetalle = jsonNode.get("crt_detalle").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(crtNombre) || !regexService.isTextNormal(crtDetalle)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    Criticidad criticidadsDb = criticidadService.encontrarCriticidadsPorNombre(crtNombre.toLowerCase(),miIdEmppal);
                    if (criticidadsDb == null) {
                        Criticidad miCriticidad = new Criticidad();
                        miCriticidad.setIdEmppal(idEmppal);
                        miCriticidad.setCrtNombre(crtNombre.toLowerCase());
                        miCriticidad.setCrtDetalle(crtDetalle.toLowerCase());
                        miCriticidad.setCrtEstado("A");
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miCriticidad.setAudFecha(fechaActual);
                        miCriticidad.setAudUsuario(miAud);
                        try {
                            criticidadService.crearCriticidad(miCriticidad);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miCriticidad, HttpStatus.OK);
                    } else {
                        criticidadsDb.setCrtEstado("A");
                        criticidadService.actualizarCriticidad(criticidadsDb);
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
                List<Criticidad> criticidadsTodosDb = criticidadService.encontrarCriticidadsFiltroPaginas(checkBoxEstado, texto, numeroDePagina,
                        numeroElementosPorPagina,miIdEmppal);
                if (criticidadsTodosDb != null) {
                    return new ResponseEntity<>(criticidadsTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" : {
                String textoC = (String) requestBody.get("texto");
                Integer criticidadTodosDbC = criticidadService.cantidadPaginasCriticidads(checkBoxEstado, textoC,miIdEmppal);
                if (criticidadTodosDbC != null) {
                    return new ResponseEntity<>(criticidadTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" : {
                Integer crtId = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbI = criticidadService.encontrarCriticidadsPorId(crtId, null,miIdEmppal);
                if (criticidadsDbI != null) {
                    return new ResponseEntity<>(criticidadsDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "borrar" : {
                Integer crtIdD = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbB = criticidadService.encontrarCriticidadsPorId(crtIdD, "A",miIdEmppal);
                if (criticidadsDbB != null) {
                    criticidadsDbB.setCrtEstado("I");
                    criticidadService.actualizarCriticidad(criticidadsDbB);
                    return new ResponseEntity<>(criticidadsDbB, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "activar" : {
                Integer crtIdDA = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbBA = criticidadService.encontrarCriticidadsPorId(crtIdDA, "I",miIdEmppal);
                if (criticidadsDbBA != null) {
                    criticidadsDbBA.setCrtEstado("A");
                    criticidadService.actualizarCriticidad(criticidadsDbBA);
                    return new ResponseEntity<>(criticidadsDbBA, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "criticidadSoloNombre" : {
                List<Criticidad> criticidadsDbS = criticidadService.encontrarCriticidadsNombres("A",miIdEmppal);
                if (criticidadsDbS != null && !criticidadsDbS.isEmpty()) {
                    return new ResponseEntity<>(criticidadsDbS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

    @PutMapping("/criticidad")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
    public ResponseEntity<?> actualizarCriticidad(@RequestBody Map<String, Object> requestBody,HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String criticidad = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(criticidad);
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        try {
            Integer crtId = jsonNode.get("crt_id").asInt();
            String crtNombre = jsonNode.get("crt_nombre").asText().trim();
            String crtDetalle = jsonNode.get("crt_detalle").asText().trim();
            Criticidad criticidadDb = criticidadService.encontrarCriticidadsPorId(crtId, "A",miIdEmppal);

            if (!regexService.isTextNormal(crtNombre) || !regexService.isTextNormal(crtDetalle)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (criticidadDb != null) {
                criticidadDb.setCrtNombre(crtNombre.toLowerCase());
                criticidadDb.setCrtDetalle(crtDetalle.toLowerCase());
                try {
                    criticidadService.actualizarCriticidad(criticidadDb);
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

}
