package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.FormularioDetalle;
import com.plprv.PlataformaProveedores.service.IFormularioDetalleServices;
import com.plprv.PlataformaProveedores.service.IFormularioServices;
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
public class FormularioDetalleRestController {
    @Autowired
    private IFormularioDetalleServices formularioDetalleService;
    private IFormularioServices formularioService;
    @Autowired
    private IRegexService regexService;
    @GetMapping("/formularioDetalle")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerFormularioDetalles(){
        List<FormularioDetalle> formularioDetallesDb = formularioDetalleService.encontrarFormularioDetalles("A");
        if(formularioDetallesDb!=null){
            return  new ResponseEntity<>(formularioDetallesDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/formularioDetalle")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = formularioDetalleService.cantidadFormularioDetalles(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String fodNombre = jsonNode.get("fod_nombre").asText().trim();
                    Integer forId = jsonNode.get("for_id").asInt();
                    String fodAdjunto = jsonNode.get("fod_adjunto").asText().trim();
                    String fodVigencia = jsonNode.get("fod_vigencia").asText().trim();
                    String fodTipo = jsonNode.get("fod_tipo").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(fodNombre) || !regexService.isSelectNumber(forId) ||
                            !regexService.isSelectText(fodVigencia) || !regexService.isSelectText(fodTipo) ||
                            !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);


                    FormularioDetalle formularioDetallesDb = formularioDetalleService.encontrarFormularioDetallesPorNombre(fodNombre.toLowerCase(),forId);
                    if(formularioDetallesDb == null) {
                       FormularioDetalle miFormularioDetalle = new FormularioDetalle();
                       miFormularioDetalle.setIdEmppal(idEmppal);
                       miFormularioDetalle.setFodNombre(fodNombre.toLowerCase());
                       miFormularioDetalle.setForId(forId);
                       miFormularioDetalle.setFodAdjunto(fodAdjunto);
                       miFormularioDetalle.setFodVigencia(fodVigencia.toLowerCase());
                       miFormularioDetalle.setFodTipo(fodTipo.toLowerCase());
                       miFormularioDetalle.setFodEstado("A");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miFormularioDetalle.setAudFecha(fechaActual);
                       miFormularioDetalle.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            formularioDetalleService.crearFormularioDetalle(miFormularioDetalle);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miFormularioDetalle, HttpStatus.OK);
                    }else{
                        formularioDetallesDb.setFodEstado("A");
                        formularioDetalleService.actualizarFormularioDetalle(formularioDetallesDb);
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
                Integer forId = (Integer) requestBody.get("forId");

                List<FormularioDetalle> formularioDetallesTodosDb = formularioDetalleService.encontrarFormularioDetallesFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina,forId);
                if(formularioDetallesTodosDb!=null){
                    return  new ResponseEntity<>(formularioDetallesTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer forIdC = (Integer) requestBody.get("forId");
                Integer formularioDetalleTodosDbC = formularioDetalleService.cantidadPaginasFormularioDetalles(checkBoxEstado,textoC, forIdC);
                if(formularioDetalleTodosDbC!=null){
                    return  new ResponseEntity<>(formularioDetalleTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer fodId = (Integer) requestBody.get("fod_id");
                FormularioDetalle formularioDetallesDbI = formularioDetalleService.encontrarFormularioDetallesPorId(fodId,null);
                if(formularioDetallesDbI!=null){
                    return  new ResponseEntity<>(formularioDetallesDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer fodIdD = (Integer) requestBody.get("fod_id");
                FormularioDetalle formularioDetallesDbB =   formularioDetalleService.encontrarFormularioDetallesPorId(fodIdD,"A");

                if(formularioDetallesDbB != null) {
                    formularioDetallesDbB.setFodEstado("I");
                    formularioDetalleService.actualizarFormularioDetalle(formularioDetallesDbB);
                    return  new ResponseEntity<>(formularioDetallesDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer fodIdDA = (Integer) requestBody.get("fod_id");
                FormularioDetalle formularioDetallesDbBA =   formularioDetalleService.encontrarFormularioDetallesPorId(fodIdDA,"I");

                if(formularioDetallesDbBA != null) {
                    formularioDetallesDbBA.setFodEstado("A");
                    formularioDetalleService.actualizarFormularioDetalle(formularioDetallesDbBA);
                    return  new ResponseEntity<>(formularioDetallesDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "formularioDetalleSoloNombre":
                List<FormularioDetalle> formularioDetallesDbS = formularioDetalleService.encontrarFormularioDetallesNombres("A");
                if(formularioDetallesDbS!=null && !formularioDetallesDbS.isEmpty()){
                    return  new ResponseEntity<>(formularioDetallesDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "formularioDetalleCheck":
                Integer forIdD = (Integer) requestBody.get("for_id");
                List<FormularioDetalle> formularioDetallesDb = formularioDetalleService.encontrarFormularioDetallesPorFormulario(forIdD,"A");
                if(formularioDetallesDb!=null){
                    return  new ResponseEntity<>(formularioDetallesDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }

            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/formularioDetalle")
    public ResponseEntity<?> actualizarFormularioDetalle(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String formularioDetalle = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(formularioDetalle);
        try {

            Integer fodId = jsonNode.get("fod_id").asInt();
            String fodNombre = jsonNode.get("fod_nombre").asText().trim();
            Integer forId = jsonNode.get("for_id").asInt();
            String fodAdjunto = jsonNode.get("fod_adjunto").asText().trim();
            String fodTipo = jsonNode.get("fod_tipo").asText().trim();
            String fodVigencia = jsonNode.get("fod_vigencia").asText().trim();

            if (!regexService.isTextNormal(fodNombre) || !regexService.isSelectNumber(forId) ||
                    !regexService.isSelectText(fodVigencia) || !regexService.isSelectText(fodTipo)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            FormularioDetalle formularioDetalleDb = formularioDetalleService.encontrarFormularioDetallesPorId(fodId, "A");

            if (formularioDetalleDb != null) {
                formularioDetalleDb.setFodNombre(fodNombre.toLowerCase());
                formularioDetalleDb.setForId(forId);
                formularioDetalleDb.setFodTipo(fodTipo.toLowerCase());
                if (fodAdjunto != null && !fodAdjunto.equals("No")){
                    formularioDetalleDb.setFodAdjunto(fodAdjunto);
                }
                formularioDetalleDb.setFodVigencia(fodVigencia.toLowerCase());
                try {
                    formularioDetalleService.actualizarFormularioDetalle(formularioDetalleDb);
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
                return new ResponseEntity<>(formularioDetalleDb, HttpStatus.OK);
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
