package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.*;
import com.plprv.PlataformaProveedores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PeriodoEvaluacionRestController {

    @Autowired
    private ICriticidadServices criticidadService;
    @Autowired
    private IPeriodoEvaluacionServices periodoEvaluacionService;

    @Autowired
    private IProveedorEvaServices proveedorEvaService;

    @Autowired
    private IProveedorServices proveedorService;

    @Autowired
    private IRegexService regexService;

    @Autowired
    private IFormularioProcesoServices formularioProcesoServices;
    @GetMapping("/periodoEvaluacion")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerPeriodoEvaluacions(){
        List<PeriodoEvaluacion> periodoEvaluacionsDb = periodoEvaluacionService.encontrarPeriodoEvaluacions("A");
        if(periodoEvaluacionsDb!=null){
            return  new ResponseEntity<>(periodoEvaluacionsDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/periodoEvaluacion")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion){
            case "cantidad":
                Integer cantidad = periodoEvaluacionService.cantidadPeriodoEvaluacions(checkBoxEstado);
                return ResponseEntity.ok(cantidad);
            case "crear":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String perNombre = jsonNode.get("per_nombre").asText().trim();
                    String perFechaEvaluacion = jsonNode.get("per_fechaevaluacion").asText().trim();
                    String perVisibilidad = jsonNode.get("per_visibilidad").asText().trim();
                    String perTipo = jsonNode.get("per_tipo").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();
                    Integer perCopia = jsonNode.get("per_copia").asInt();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(perNombre) || !regexService.isTextNormal(perFechaEvaluacion) ||
                            !regexService.isSelectText(perVisibilidad) || !regexService.isSelectText(perTipo) ||
                            !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    PeriodoEvaluacion periodoEvaluacionsDb = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorNombre(perNombre.toLowerCase());
                    if(periodoEvaluacionsDb == null) {
                       PeriodoEvaluacion miPeriodoEvaluacion = new PeriodoEvaluacion();
                       miPeriodoEvaluacion.setIdEmppal(idEmppal);
                       miPeriodoEvaluacion.setPerNombre(perNombre.toLowerCase());
                       miPeriodoEvaluacion.setPerFechaEvaluacion(perFechaEvaluacion);
                       miPeriodoEvaluacion.setPerVisibilidad(perVisibilidad.toLowerCase());
                       miPeriodoEvaluacion.setPerTipo(perTipo.toLowerCase());
                       miPeriodoEvaluacion.setPerEstado("A");
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miPeriodoEvaluacion.setAudFecha(fechaActual);
                       miPeriodoEvaluacion.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            periodoEvaluacionService.crearPeriodoEvaluacion(miPeriodoEvaluacion);
                            List<FormularioProceso> miFormularioProceso = formularioProcesoServices.encontrarFormulariosPorPeriodo(perCopia);
                            for (FormularioProceso formulario : miFormularioProceso) {
                                FormularioProceso formularioDuplicado = new FormularioProceso();
                                formularioDuplicado.setTdcTd(formulario.getTdcTd());
                                formularioDuplicado.setForId(formulario.getForId());
                                formularioDuplicado.setPerId(miPeriodoEvaluacion.getPerId());
                                formularioDuplicado.setFodId(formulario.getFodId());
                                formularioDuplicado.setCrtId(formulario.getCrtId());
                                formularioDuplicado.setProId(formulario.getProId());
                                formularioDuplicado.setSprId(formulario.getSprId());
                                formularioDuplicado.setIdEmppal(idEmppal);
                                formularioDuplicado.setFopEstado("A");
                                formularioDuplicado.setAudFecha(fechaActual);
                                formularioDuplicado.setAudUsuario(audUsuario);
                                formularioProcesoServices.crearFormularioProceso(formularioDuplicado);
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
                        try {
                            JsonNode selectCriticidadNode = jsonNode.get("select_criticidad");
                            List<Criticidad> criticidadsDb = criticidadService.encontrarCriticidads("A");

                            List<Criticidad> siAplican = new ArrayList<>();
                            List<Criticidad> noAplican = new ArrayList<>();

                            for (Criticidad element : criticidadsDb) {
                                boolean encontrado = false;
                                for (JsonNode elementC : selectCriticidadNode) {

                                    if (elementC.asInt() == (element.getCrtId ())) {
                                        siAplican.add(element);
                                        encontrado = true;
                                        break;
                                    }
                                }
                                if (!encontrado) {
                                    noAplican.add(element);
                                }
                            }
                            if (siAplican != null ) {
                                for (Criticidad element : siAplican) {
                                    List<Proveedor> miProveedor = proveedorService.encontrarProveedoresPorCriticidad("A",element.getCrtId() );
                                    if(miProveedor!=null){
                                        for (Proveedor proveedor : miProveedor) {
                                            ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(miPeriodoEvaluacion.getPerId(), proveedor.getPrvId());
                                            if(proveedorEvasDb == null) {
                                                ProveedorEva miProveedorEva = new ProveedorEva();
                                                miProveedorEva.setIdEmppal(idEmppal);
                                                miProveedorEva.setPerId(miPeriodoEvaluacion.getPerId());
                                                miProveedorEva.setPrvId(proveedor.getPrvId());
                                                miProveedorEva.setPreResultado(0);
                                                miProveedorEva.setPreContinua("SI");
                                                miProveedorEva.setPreObservacion("");
                                                miProveedorEva.setPreEstado("NI");
                                                miProveedorEva.setAudFecha(fechaActual);
                                                miProveedorEva.setAudUsuario(audUsuario);
                                                try {
                                                    proveedorEvaService.crearProveedorEva(miProveedorEva);
                                                } catch (DataIntegrityViolationException e) {
                                                    if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                                        String data = "dato_existente";
                                                        return new ResponseEntity<>( data, HttpStatus.OK);
                                                    } else {
                                                        String data = "error_sql";
                                                        return new ResponseEntity<>( data, HttpStatus.OK);
                                                    }
                                                }
                                            }else{
                                                return ResponseEntity.ok("ya_existe");
                                            }
                                        }
                                    }else {
                                        return ResponseEntity.ok("sin_proveedores_crt");
                                    }
                                }
                            } else {
                                return ResponseEntity.ok("select_criticidad_no_existe");
                            }
                            if (noAplican != null ) {
                                for (Criticidad element : noAplican) {
                                    List<Proveedor> miProveedor = proveedorService.encontrarProveedoresPorCriticidad("A",element.getCrtId() );
                                    if(miProveedor!=null){
                                        for (Proveedor proveedor : miProveedor) {
                                            ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(miPeriodoEvaluacion.getPerId(), proveedor.getPrvId());
                                            if(proveedorEvasDb == null) {
                                                ProveedorEva miProveedorEva = new ProveedorEva();
                                                miProveedorEva.setIdEmppal(idEmppal);
                                                miProveedorEva.setPerId(miPeriodoEvaluacion.getPerId());
                                                miProveedorEva.setPrvId(proveedor.getPrvId());
                                                miProveedorEva.setPreResultado(0);
                                                miProveedorEva.setPreContinua("");
                                                miProveedorEva.setPreObservacion("");
                                                miProveedorEva.setPreEstado("NA");
                                                miProveedorEva.setAudFecha(fechaActual);
                                                miProveedorEva.setAudUsuario(audUsuario);
                                                try {
                                                    proveedorEvaService.crearProveedorEva(miProveedorEva);
                                                } catch (DataIntegrityViolationException e) {
                                                    if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                                        String data = "dato_existente";
                                                        return new ResponseEntity<>( data, HttpStatus.OK);
                                                    } else {
                                                        String data = "error_sql";
                                                        return new ResponseEntity<>( data, HttpStatus.OK);
                                                    }
                                                }
                                            }else{
                                                return ResponseEntity.ok("ya_existe");
                                            }
                                        }
                                    }else {
                                        return ResponseEntity.ok("sin_proveedores_crt");
                                    }
                                }
                            } else {
                                return ResponseEntity.ok("select_criticidad_no_existe");
                            }

                        }catch (DataIntegrityViolationException e){
                            return ResponseEntity.ok(e.getMessage());
                        }
                       return new ResponseEntity<>(miPeriodoEvaluacion, HttpStatus.OK);
                    }else{
                        periodoEvaluacionsDb.setPerEstado("A");
                        periodoEvaluacionService.actualizarPeriodoEvaluacion(periodoEvaluacionsDb);
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

                List<PeriodoEvaluacion> periodoEvaluacionsTodosDb = periodoEvaluacionService.encontrarPeriodoEvaluacionsFiltroPaginas(checkBoxEstado,texto,numeroDePagina,numeroElementosPorPagina);
                if(periodoEvaluacionsTodosDb!=null){
                    return  new ResponseEntity<>(periodoEvaluacionsTodosDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "cantidadDePaginas":
                String textoC = (String) requestBody.get("texto");
                Integer periodoEvaluacionTodosDbC = periodoEvaluacionService.cantidadPaginasPeriodoEvaluacions(checkBoxEstado,textoC);
                if(periodoEvaluacionTodosDbC!=null){
                    return  new ResponseEntity<>(periodoEvaluacionTodosDbC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "obtenerId":
                Integer perId = (Integer) requestBody.get("per_id");
                PeriodoEvaluacion periodoEvaluacionsDbI = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perId,null);
                if(periodoEvaluacionsDbI!=null){
                    return  new ResponseEntity<>(periodoEvaluacionsDbI , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "borrar":
                Integer perIdD = (Integer) requestBody.get("per_id");
                PeriodoEvaluacion periodoEvaluacionsDbB =   periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perIdD,"A");

                if(periodoEvaluacionsDbB != null) {
                    periodoEvaluacionsDbB.setPerEstado("I");
                    periodoEvaluacionService.actualizarPeriodoEvaluacion(periodoEvaluacionsDbB);
                    return  new ResponseEntity<>(periodoEvaluacionsDbB , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "activar":
                Integer perIdDA = (Integer) requestBody.get("per_id");
                PeriodoEvaluacion periodoEvaluacionsDbBA =   periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perIdDA,"I");

                if(periodoEvaluacionsDbBA != null) {
                    periodoEvaluacionsDbBA.setPerEstado("A");
                    periodoEvaluacionService.actualizarPeriodoEvaluacion(periodoEvaluacionsDbBA);
                    return  new ResponseEntity<>(periodoEvaluacionsDbBA , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            case "periodoEvaluacionSoloNombre":
                String perTipo = (String) requestBody.get("perTipo");
                List<PeriodoEvaluacion> periodoEvaluacionsDbS = periodoEvaluacionService.encontrarPeriodoEvaluacionsNombres(perTipo);
                if(periodoEvaluacionsDbS!=null && !periodoEvaluacionsDbS.isEmpty()){
                    return  new ResponseEntity<>(periodoEvaluacionsDbS , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(null,HttpStatus.OK);
                }
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

    @PutMapping("/periodoEvaluacion")
    public ResponseEntity<?> actualizarPeriodoEvaluacion(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String periodoEvaluacion = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(periodoEvaluacion);
        try {

            Integer perId = jsonNode.get("per_id").asInt();
            String perNombre = jsonNode.get("per_nombre").asText().trim();
            String perFechaEvaluacion = jsonNode.get("per_fechaevaluacion").asText().trim();
            String perVisibilidad = jsonNode.get("per_visibilidad").asText().trim();
            String perTipo = jsonNode.get("per_tipo").asText().trim();
            PeriodoEvaluacion periodoEvaluacionDb = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perId, "A");

            if (!regexService.isTextNormal(perNombre) || !regexService.isTextNormal(perFechaEvaluacion) ||
                    !regexService.isSelectText(perVisibilidad) || !regexService.isSelectText(perTipo)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (periodoEvaluacionDb != null) {
                periodoEvaluacionDb.setPerNombre(perNombre.toLowerCase());
                periodoEvaluacionDb.setPerFechaEvaluacion(perFechaEvaluacion);
                periodoEvaluacionDb.setPerVisibilidad(perVisibilidad.toLowerCase());
//                periodoEvaluacionDb.setPerTipo(perTipo);
                try {
                    periodoEvaluacionService.actualizarPeriodoEvaluacion(periodoEvaluacionDb);
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
