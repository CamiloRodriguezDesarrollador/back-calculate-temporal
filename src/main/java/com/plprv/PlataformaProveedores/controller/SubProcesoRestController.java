package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import com.plprv.PlataformaProveedores.service.IProcesoServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ISubProcesoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SubProcesoRestController {
    @Autowired
    private ISubProcesoServices subProcesoService;

    @Autowired
    private IRegexService regexService;

    @GetMapping("/subProceso")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerProcesos(){
        List<SubProceso> subProcesosDb = subProcesoService.encontrarSubProcesos("A");
        if(subProcesosDb!=null ){
            return  new ResponseEntity<>(subProcesosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/subProceso")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = subProcesoService.cantidadSubProcesos(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String sprNombre = jsonNode.get("spr_nombre").asText().trim();
                    Integer proId = jsonNode.get("pro_id").asInt();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(sprNombre) || !regexService.isSelectNumber(proId) ||
                            !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    SubProceso subProcesosDb = subProcesoService.encontrarSubProcesosPorNombre(sprNombre.toLowerCase(), proId);

                    if(subProcesosDb == null) {
                       SubProceso miSubProceso = new SubProceso();
                       miSubProceso.setIdEmppal(idEmppal);
                       miSubProceso.setSprNombre(sprNombre.toLowerCase());
                       miSubProceso.setProId(proId);
                       miSubProceso.setSprEstado("A");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miSubProceso.setAudFecha(fechaActual);
                       miSubProceso.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            subProcesoService.crearSubProceso(miSubProceso);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( ((SQLException) e.getCause().getCause()).getErrorCode(), HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miSubProceso, HttpStatus.OK);
                    }else{
                        subProcesosDb.setSprEstado("A");
                        subProcesoService.actualizarSubProceso(subProcesosDb);
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
                Integer proId = (Integer) requestBody.get("proId");

                List<SubProceso> subProcesosTodosDb = subProcesoService.encontrarSubProcesosFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina, proId);
                if(subProcesosTodosDb!=null ){
                    return  new ResponseEntity<>(subProcesosTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "informacionTotalTabla":
                Integer proIdT = 0;

                String textoT = (String) requestBody.get("texto");
                try{
                    proIdT = (Integer) requestBody.get("proId");
                } catch (Exception e) {
                    return  new ResponseEntity<>("" , HttpStatus.OK);
                }
                List<SubProceso> subProcesosTodosDbT = subProcesoService.encontrarSubProcesosFiltro("A",textoT, proIdT);
                if(subProcesosTodosDbT!=null ){
                    return  new ResponseEntity<>(subProcesosTodosDbT , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer proIdC = (Integer) requestBody.get("proId");
                Integer subProcesosTodosDbC = subProcesoService.cantidadPaginasSubProcesos(checkBoxEstado,textoC,proIdC);
                if(subProcesosTodosDbC!=null){
                    return  new ResponseEntity<>(subProcesosTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer sprId = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbI = subProcesoService.encontrarSubProcesosPorId(sprId,null);
                if(subProcesosDbI!=null){
                    return  new ResponseEntity<>(subProcesosDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer sprIdD = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbB =   subProcesoService.encontrarSubProcesosPorId(sprIdD,"A");

                if(subProcesosDbB != null) {
                    subProcesosDbB.setSprEstado("I");
                    subProcesoService.actualizarSubProceso(subProcesosDbB);
                    return  new ResponseEntity<>(subProcesosDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer sprIdDA = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbBA =   subProcesoService.encontrarSubProcesosPorId(sprIdDA,"I");

                if(subProcesosDbBA != null) {
                    subProcesosDbBA.setSprEstado("A");
                    subProcesoService.actualizarSubProceso(subProcesosDbBA);
                    return  new ResponseEntity<>(subProcesosDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "subProcesoSoloNombre":
                List<SubProceso> subProcesosDbS = subProcesoService.encontrarSubProcesosNombres("A");
                if(subProcesosDbS!=null && !subProcesosDbS.isEmpty()){
                    return  new ResponseEntity<>(subProcesosDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }
    }
    @PutMapping("/subProceso")
    public ResponseEntity<?> actualizarProceso(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String subProceso = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(subProceso);

        try{
            Integer sprId = jsonNode.get("spr_id").asInt();
            String sprNombre = jsonNode.get("spr_nombre").asText().trim();
            Integer proId = jsonNode.get("pro_id").asInt();
            SubProceso subProcesoDb = subProcesoService.encontrarSubProcesosPorId(sprId,"A");

            if (!regexService.isTextNormal(sprNombre) || !regexService.isSelectNumber(proId)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);


            if(subProcesoDb != null) {
                subProcesoDb.setSprNombre(sprNombre.toLowerCase());
                subProcesoDb.setProId(proId);
                try {
                    subProcesoService.actualizarSubProceso(subProcesoDb);
                } catch (DataIntegrityViolationException e) {
                    if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                        String data = "dato_existente";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        String data = "error_sql";
                        return new ResponseEntity<>( ((SQLException) e.getCause().getCause()).getErrorCode(), HttpStatus.OK);
                    }
                }
                String data = "editado";
                return new ResponseEntity<>(data, HttpStatus.OK);
            }else {
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
