package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Formulario;
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
public class FormularioRestController {
    @Autowired
    private IFormularioServices formularioService;
    @Autowired
    private IRegexService regexService;
    @GetMapping("/formulario")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerFormularios(){
        List<Formulario> formulariosDb = formularioService.encontrarFormularios("A");
        if(formulariosDb!=null){
            return  new ResponseEntity<>(formulariosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/formulario")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = formularioService.cantidadFormularios(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String forNombre = jsonNode.get("for_nombre").asText().trim();
                    String forDetalle = jsonNode.get("for_detalle").asText().trim();
                    String forTipo = jsonNode.get("for_tipo").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(forNombre) || !regexService.isObservacion(forDetalle) ||
                            !regexService.isSelectText(forTipo) ||
                            !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);


                    Formulario formulariosDb = formularioService.encontrarFormulariosPorNombre(forNombre.toLowerCase());
                    if(formulariosDb == null) {
                       Formulario miFormulario = new Formulario();
                       miFormulario.setIdEmppal(idEmppal);
                       miFormulario.setForNombre(forNombre.toLowerCase());
                       miFormulario.setForDetalle(forDetalle.toLowerCase());
                       miFormulario.setForTipo(forTipo.toLowerCase());
                       miFormulario.setForEstado("A");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miFormulario.setAudFecha(fechaActual);
                       miFormulario.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            formularioService.crearFormulario(miFormulario);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miFormulario, HttpStatus.OK);
                    }else{
                        formulariosDb.setForEstado("A");
                        formularioService.actualizarFormulario(formulariosDb);
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

                List<Formulario> formulariosTodosDb = formularioService.encontrarFormulariosFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(formulariosTodosDb!=null){
                    return  new ResponseEntity<>(formulariosTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer formularioTodosDbC = formularioService.cantidadPaginasFormularios(checkBoxEstado,textoC);
                if(formularioTodosDbC!=null){
                    return  new ResponseEntity<>(formularioTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer forId = (Integer) requestBody.get("for_id");
                Formulario formulariosDbI = formularioService.encontrarFormulariosPorId(forId,null);
                if(formulariosDbI!=null){
                    return  new ResponseEntity<>(formulariosDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer forIdD = (Integer) requestBody.get("for_id");
                Formulario formulariosDbB =   formularioService.encontrarFormulariosPorId(forIdD,"A");

                if(formulariosDbB != null) {
                    formulariosDbB.setForEstado("I");
                    formularioService.actualizarFormulario(formulariosDbB);
                    return  new ResponseEntity<>(formulariosDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer forIdDA = (Integer) requestBody.get("for_id");
                Formulario formulariosDbBA =   formularioService.encontrarFormulariosPorId(forIdDA,"I");

                if(formulariosDbBA != null) {
                    formulariosDbBA.setForEstado("A");
                    formularioService.actualizarFormulario(formulariosDbBA);
                    return  new ResponseEntity<>(formulariosDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "formularioSoloNombre":
                List<Formulario> formulariosDbS = formularioService.encontrarFormulariosNombres("A");
                if(formulariosDbS!=null && !formulariosDbS.isEmpty()){
                    return  new ResponseEntity<>(formulariosDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/formulario")
    public ResponseEntity<?> actualizarFormulario(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String formulario = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(formulario);
        try {

            Integer forId = jsonNode.get("for_id").asInt();
            String forNombre = jsonNode.get("for_nombre").asText().trim();
            String forDetalle = jsonNode.get("for_detalle").asText().trim();
            String forTipo = jsonNode.get("for_tipo").asText().trim();
            Formulario formularioDb = formularioService.encontrarFormulariosPorId(forId, "A");

            if (!regexService.isTextNormal(forNombre) || !regexService.isTextNormal(forDetalle) ||
                    !regexService.isSelectText(forTipo)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (formularioDb != null) {
                formularioDb.setForNombre(forNombre.toLowerCase());
                formularioDb.setForDetalle(forDetalle.toLowerCase());
                formularioDb.setForTipo(forTipo.toLowerCase());
                try {
                    formularioService.actualizarFormulario(formularioDb);
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
