package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Criticidad;
import com.plprv.PlataformaProveedores.service.ICriticidadServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/criticidad")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerCriticidads(){
        List<Criticidad> criticidadsDb = criticidadService.encontrarCriticidads("A");
        if(criticidadsDb!=null){
            return  new ResponseEntity<>(criticidadsDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/criticidad")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = criticidadService.cantidadCriticidads(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String crtNombre = jsonNode.get("crt_nombre").asText().trim();
                    String crtDetalle = jsonNode.get("crt_detalle").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(crtNombre) || !regexService.isTextNormal(crtDetalle) ||
                            !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    Criticidad criticidadsDb = criticidadService.encontrarCriticidadsPorNombre(crtNombre.toLowerCase());
                    if(criticidadsDb == null) {
                       Criticidad miCriticidad = new Criticidad();
                       miCriticidad.setIdEmppal(idEmppal);
                       miCriticidad.setCrtNombre(crtNombre.toLowerCase());
                       miCriticidad.setCrtDetalle(crtDetalle.toLowerCase());
                       miCriticidad.setCrtEstado("A");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miCriticidad.setAudFecha(fechaActual);
                       miCriticidad.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            criticidadService.crearCriticidad(miCriticidad);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miCriticidad, HttpStatus.OK);
                    }else{
                        criticidadsDb.setCrtEstado("A");
                        criticidadService.actualizarCriticidad(criticidadsDb);
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

                List<Criticidad> criticidadsTodosDb = criticidadService.encontrarCriticidadsFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(criticidadsTodosDb!=null){
                    return  new ResponseEntity<>(criticidadsTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer criticidadTodosDbC = criticidadService.cantidadPaginasCriticidads(checkBoxEstado,textoC);
                if(criticidadTodosDbC!=null){
                    return  new ResponseEntity<>(criticidadTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer crtId = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbI = criticidadService.encontrarCriticidadsPorId(crtId,null);
                if(criticidadsDbI!=null){
                    return  new ResponseEntity<>(criticidadsDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer crtIdD = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbB =   criticidadService.encontrarCriticidadsPorId(crtIdD,"A");

                if(criticidadsDbB != null) {
                    criticidadsDbB.setCrtEstado("I");
                    criticidadService.actualizarCriticidad(criticidadsDbB);
                    return  new ResponseEntity<>(criticidadsDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer crtIdDA = (Integer) requestBody.get("crt_id");
                Criticidad criticidadsDbBA =   criticidadService.encontrarCriticidadsPorId(crtIdDA,"I");
                if(criticidadsDbBA != null) {
                    criticidadsDbBA.setCrtEstado("A");
                    criticidadService.actualizarCriticidad(criticidadsDbBA);
                    return  new ResponseEntity<>(criticidadsDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "criticidadSoloNombre":
                List<Criticidad> criticidadsDbS = criticidadService.encontrarCriticidadsNombres("A");
                if(criticidadsDbS!=null && !criticidadsDbS.isEmpty()){
                    return  new ResponseEntity<>(criticidadsDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/criticidad")
    public ResponseEntity<?> actualizarCriticidad(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String criticidad = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(criticidad);
        try {
            Integer crtId = jsonNode.get("crt_id").asInt();
            String crtNombre = jsonNode.get("crt_nombre").asText().trim();
            String crtDetalle = jsonNode.get("crt_detalle").asText().trim();
            Criticidad criticidadDb = criticidadService.encontrarCriticidadsPorId(crtId, "A");

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
                        System.out.println(e.getMessage());
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        String data = "error_sql";
                        System.out.println(2);
                        System.out.println(((SQLException) e.getCause().getCause()).getErrorCode());

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
