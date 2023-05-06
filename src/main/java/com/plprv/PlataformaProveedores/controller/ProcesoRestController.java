package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import com.plprv.PlataformaProveedores.service.IProcesoServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ObtenerUsuarioAud;
import jakarta.servlet.http.HttpServletRequest;
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
public class ProcesoRestController {
    @Autowired
    private IProcesoServices procesoService;

    @Autowired
    private IRegexService regexService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;
    @GetMapping("/proceso")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerProcesos(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<Proceso> procesosDb = procesoService.encontrarProcesos("A",miIdEmppal);
        if(procesosDb!=null){
            return  new ResponseEntity<>(procesosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }
    @PostMapping("/proceso")
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
            case "cantidad" -> {
                Integer cantidad = procesoService.cantidadProcesos(checkBoxEstado,miIdEmppal);
                return ResponseEntity.ok(cantidad);
            }
            case "crear" -> {
                try {

                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String proNombre = jsonNode.get("pro_nombre").asText().trim().trim();
                    String proResponsable = jsonNode.get("pro_responsable").asText().trim();
                    String proCorreo = jsonNode.get("pro_correo").asText().trim().trim();
                    String proCelular = jsonNode.get("pro_celular").asText().trim().trim();
                    String proCargo = jsonNode.get("pro_cargo").asText().trim().trim();

                    if (!regexService.isTextNormal(proNombre) || !regexService.isTextNormal(proResponsable) || !regexService.isMail(proCorreo) ||
                            !regexService.isTextNormal(proCelular) || !regexService.isId(idEmppal) || !regexService.isTextNormal(proCargo)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    Proceso procesosDb = procesoService.encontrarProcesosPorNombre(proNombre.toLowerCase(),miIdEmppal);

                    if (procesosDb == null) {
                        Proceso miProceso = new Proceso();
                        miProceso.setIdEmppal(idEmppal);
                        miProceso.setProNombre(proNombre.toLowerCase());
                        miProceso.setProResponsable(proResponsable.toLowerCase());
                        miProceso.setProCelular(proCelular);
                        miProceso.setProCorreo(proCorreo.toLowerCase());
                        miProceso.setProCargo(proCargo.toLowerCase());
                        miProceso.setProEstado("A");
                        Date fechaActual = new Date();
                        Set<Date> conjuntoFechas = new HashSet<>();
                        conjuntoFechas.add(fechaActual);
                        miProceso.setAudFecha(fechaActual);
                        miProceso.setAudUsuario(miAud);
                        try {
                            procesoService.crearProceso(miProceso);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miProceso, HttpStatus.OK);
                    } else {
                        procesosDb.setProEstado("A");
                        procesoService.actualizarProceso(procesosDb);
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
            case "informacionTotal" -> {
                Integer numeroDePagina = (Integer) requestBody.get("numeroDePagina");
                Integer numeroElementosPorPagina = (Integer) requestBody.get("numeroElementosPorPagina");
                String texto = (String) requestBody.get("texto");
                List<Proceso> procesosTodosDb = procesoService.encontrarProcesosFiltroPaginas(checkBoxEstado, texto, numeroDePagina, numeroElementosPorPagina,miIdEmppal);
                if (procesosTodosDb != null) {
                    return new ResponseEntity<>(procesosTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" -> {
                String textoC = (String) requestBody.get("texto");
                Integer procesosTodosDbC = procesoService.cantidadPaginasProcesos(checkBoxEstado, textoC,miIdEmppal);
                if (procesosTodosDbC != null) {
                    return new ResponseEntity<>(procesosTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" -> {
                Integer proId = (Integer) requestBody.get("pro_id");
                Proceso procesosDbI = procesoService.encontrarProcesosPorId(proId, null,miIdEmppal);
                if (procesosDbI != null) {
                    return new ResponseEntity<>(procesosDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "borrar" -> {
                Integer proIdD = (Integer) requestBody.get("pro_id");
                Proceso procesosDbB = procesoService.encontrarProcesosPorId(proIdD, "A",miIdEmppal);
                if (procesosDbB != null) {
                    procesosDbB.setProEstado("I");
                    procesoService.actualizarProceso(procesosDbB);
                    return new ResponseEntity<>(procesosDbB, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "activar" -> {
                Integer proIdDA = (Integer) requestBody.get("pro_id");
                Proceso procesosDbBA = procesoService.encontrarProcesosPorId(proIdDA, "I",miIdEmppal);
                if (procesosDbBA != null) {
                    procesosDbBA.setProEstado("A");
                    procesoService.actualizarProceso(procesosDbBA);
                    return new ResponseEntity<>(procesosDbBA, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "procesoSoloNombre" -> {
                List<Proceso> procesosDbS = procesoService.encontrarProcesosNombres("A",miIdEmppal);
                if (procesosDbS != null && !procesosDbS.isEmpty()) {
                    return new ResponseEntity<>(procesosDbS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            default -> {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

    @PostMapping("/procesoL")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String opcion = (String) requestBody.get("opcion");
        Integer idEmppal = (Integer) requestBody.get("idEmppal");
        if (opcion.equals("informacionTotalTabla")) {
            List<Proceso> procesosIdEmppal = procesoService.encontrarProcesos("A",idEmppal);
            if (procesosIdEmppal != null) {
                return new ResponseEntity<>(procesosIdEmppal, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }
        return ResponseEntity.ok("Opcion no encontrada");
    }

    @PutMapping("/proceso")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
    public ResponseEntity<?> actualizarProceso(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String proceso = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(proceso);
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        try {
            Integer proId = jsonNode.get("pro_id").asInt();
            String proNombre = jsonNode.get("pro_nombre").asText().trim();
            String proResponsable = jsonNode.get("pro_responsable").asText().trim();
            String proCorreo = jsonNode.get("pro_correo").asText().trim();
            String proCelular = jsonNode.get("pro_celular").asText().trim();
            String proCargo = jsonNode.get("pro_cargo").asText().trim();
            Proceso procesoDb = procesoService.encontrarProcesosPorId(proId, "A",miIdEmppal);

            if (!regexService.isTextNormal(proNombre) || !regexService.isTextNormal(proResponsable) || !regexService.isMail(proCorreo) ||
                    !regexService.isTextNormal(proCelular) || !regexService.isTextNormal(proCargo)
            )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

            if (procesoDb != null) {
                procesoDb.setProNombre(proNombre.toLowerCase());
                procesoDb.setProResponsable(proResponsable.toLowerCase());
                procesoDb.setProCorreo(proCorreo.toLowerCase());
                procesoDb.setProCelular(proCelular.toLowerCase());
                procesoDb.setProCargo(proCargo.toLowerCase());
                try {
                    procesoService.actualizarProceso(procesoDb);
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
