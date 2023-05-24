package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.SubProceso;
import com.plprv.PlataformaProveedores.service.IRegexService;
import com.plprv.PlataformaProveedores.service.ISubProcesoServices;
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
public class SubProcesoRestController {
    @Autowired
    private ISubProcesoServices subProcesoService;

    @Autowired
    private IRegexService regexService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;
    @GetMapping("/subProceso")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> obtenerProcesos(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<SubProceso> subProcesosDb = subProcesoService.encontrarSubProcesos("A",miIdEmppal);
        if(subProcesosDb!=null ){
            return  new ResponseEntity<>(subProcesosDb , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }


    @PostMapping("/subProceso")
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
                Integer cantidad = subProcesoService.cantidadSubProcesos(checkBoxEstado,miIdEmppal);
                return ResponseEntity.ok(cantidad);
            }
            case "crear" : {
                try {
                    int idEmppal = jsonNode.get("id_emppal").asInt();
                    String sprNombre = jsonNode.get("spr_nombre").asText().trim();
                    int proId = jsonNode.get("pro_id").asInt();

                    if (!regexService.isId(idEmppal) || !regexService.isTextNormal(sprNombre) || !regexService.isSelectNumber(proId)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    SubProceso subProcesosDb = subProcesoService.encontrarSubProcesosPorNombre(sprNombre.toLowerCase(), proId,miIdEmppal);

                    if (subProcesosDb == null) {
                        SubProceso miSubProceso = new SubProceso();
                        miSubProceso.setIdEmppal(idEmppal);
                        miSubProceso.setSprNombre(sprNombre.toLowerCase());
                        miSubProceso.setProId(proId);
                        miSubProceso.setSprEstado("A");
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miSubProceso.setAudFecha(fechaActual);
                        miSubProceso.setAudUsuario(miAud);
                        try {
                            subProcesoService.crearSubProceso(miSubProceso);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(((SQLException) e.getCause().getCause()).getErrorCode(), HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miSubProceso, HttpStatus.OK);
                    } else {
                        subProcesosDb.setSprEstado("A");
                        subProcesoService.actualizarSubProceso(subProcesosDb);
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
                Integer proId = (Integer) requestBody.get("proId");
                List<SubProceso> subProcesosTodosDb = subProcesoService.encontrarSubProcesosFiltroPaginas(checkBoxEstado,
                        texto, numeroDePagina, numeroElementosPorPagina, proId,miIdEmppal);
                if (subProcesosTodosDb != null) {
                    return new ResponseEntity<>(subProcesosTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" : {
                String textoC = (String) requestBody.get("texto");
                Integer proIdC = (Integer) requestBody.get("proId");
                Integer subProcesosTodosDbC = subProcesoService.cantidadPaginasSubProcesos(checkBoxEstado, textoC, proIdC,miIdEmppal);
                if (subProcesosTodosDbC != null) {
                    return new ResponseEntity<>(subProcesosTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" : {
                Integer sprId = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbI = subProcesoService.encontrarSubProcesosPorId(sprId, null,miIdEmppal);
                if (subProcesosDbI != null) {
                    return new ResponseEntity<>(subProcesosDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "borrar" : {
                Integer sprIdD = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbB = subProcesoService.encontrarSubProcesosPorId(sprIdD, "A",miIdEmppal);
                if (subProcesosDbB != null) {
                    subProcesosDbB.setSprEstado("I");
                    subProcesoService.actualizarSubProceso(subProcesosDbB);
                    return new ResponseEntity<>(subProcesosDbB, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "activar" : {
                Integer sprIdDA = (Integer) requestBody.get("spr_id");
                SubProceso subProcesosDbBA = subProcesoService.encontrarSubProcesosPorId(sprIdDA, "I",miIdEmppal);
                if (subProcesosDbBA != null) {
                    subProcesosDbBA.setSprEstado("A");
                    subProcesoService.actualizarSubProceso(subProcesosDbBA);
                    return new ResponseEntity<>(subProcesosDbBA, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "subProcesoSoloNombre" : {
                List<SubProceso> subProcesosDbS = subProcesoService.encontrarSubProcesosNombres("A",miIdEmppal);
                if (subProcesosDbS != null && !subProcesosDbS.isEmpty()) {
                    return new ResponseEntity<>(subProcesosDbS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }
    }

    @PostMapping("/subProcesoL")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String opcion = (String) requestBody.get("opcion");
        Integer miIdEmppal = 0;
        try{
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        } catch (Exception e) {
        }


        if (opcion.equals("informacionTotalTabla")) {
            Integer proIdT = 0;

            String textoT = (String) requestBody.get("texto");
            try {
                proIdT = (Integer) requestBody.get("proId");
            } catch (Exception e) {
                return new ResponseEntity<>("", HttpStatus.OK);
            }
            List<SubProceso> subProcesosTodosDbT = subProcesoService.encontrarSubProcesosFiltro("A", textoT, proIdT,miIdEmppal);
            if (subProcesosTodosDbT != null) {
                return new ResponseEntity<>(subProcesosTodosDbT, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }
        if (opcion.equals("informacionTotalTablaInicio")) {
            Integer proIdT = (Integer) requestBody.get("proId");
            String textoT = (String) requestBody.get("texto");
            Integer idEmppal = (Integer) requestBody.get("idEmppal");
            List<SubProceso> subProcesosTodosDbT = subProcesoService.encontrarSubProcesosFiltro("A", textoT, proIdT,idEmppal);
            if (subProcesosTodosDbT != null) {
                return new ResponseEntity<>(subProcesosTodosDbT, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
        }
        return ResponseEntity.ok("Opcion no encontrada");
    }
    @PutMapping("/subProceso")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
    public ResponseEntity<?> actualizarProceso(@RequestBody Map<String, Object> requestBody,HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String subProceso = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(subProceso);
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);

        try{
            int sprId = jsonNode.get("spr_id").asInt();
            String sprNombre = jsonNode.get("spr_nombre").asText().trim();
            int proId = jsonNode.get("pro_id").asInt();
            SubProceso subProcesoDb = subProcesoService.encontrarSubProcesosPorId(sprId,"A",miIdEmppal);

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
