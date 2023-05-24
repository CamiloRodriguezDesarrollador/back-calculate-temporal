package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import com.plprv.PlataformaProveedores.service.IFormularioProcesoServices;
import com.plprv.PlataformaProveedores.service.ObtenerUsuarioAud;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FormularioProcesoRestController {
    @Autowired
    private IFormularioProcesoServices formularioProcesoService;
    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;
    @GetMapping("/formularioProceso")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> obtenerFormularioProcesos(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        List<FormularioProceso> formularioProcesosDb = formularioProcesoService.encontrarFormularioProceso(miIdEmppal);
        if (formularioProcesosDb != null) {
            return new ResponseEntity<>(formularioProcesosDb, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }

    @PostMapping("/formularioProceso")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("form"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");

        if (opcion.equals("cambiarEstado")) {
            try {
                String tdcTd = jsonNode.get("tdc_td").asText().trim();
                int forId = jsonNode.get("for_id").asInt();
                int perId = jsonNode.get("per_id").asInt();
                int fodId = jsonNode.get("fod_id").asInt();
                int crtId = jsonNode.get("crt_id").asInt();
                int proId = jsonNode.get("pro_id").asInt();
                int sprId = jsonNode.get("spr_id").asInt();
                int idEmppal = jsonNode.get("id_emppal").asInt();
                FormularioProceso formularioProcesoDb = formularioProcesoService.encontrarFormularioPorParametros(forId, fodId, proId, sprId, perId, crtId, tdcTd, miIdEmppal);
                if (formularioProcesoDb == null) {
                    FormularioProceso miFormularioProceso = new FormularioProceso();
                    miFormularioProceso.setTdcTd(tdcTd);
                    miFormularioProceso.setForId(forId);
                    miFormularioProceso.setPerId(perId);
                    miFormularioProceso.setFodId(fodId);
                    miFormularioProceso.setCrtId(crtId);
                    miFormularioProceso.setProId(proId);
                    miFormularioProceso.setSprId(sprId);
                    miFormularioProceso.setIdEmppal(idEmppal);
                    miFormularioProceso.setFopEstado("A");
                    Date fechaActual = new Date();
                    Set<Date> conjuntoFechas = new HashSet<>();
                    conjuntoFechas.add(fechaActual);
                    miFormularioProceso.setAudFecha(fechaActual);
                    miFormularioProceso.setAudUsuario(miAud);
                    formularioProcesoService.crearFormularioProceso(miFormularioProceso);
                    String data = "creado";
                    return new ResponseEntity<>(data, HttpStatus.OK);
                } else {
                    if (formularioProcesoDb.getFopEstado().equals("A")) {
                        formularioProcesoDb.setFopEstado("I");
                        formularioProcesoService.cambiarEstadoFormularioProceso(formularioProcesoDb);
                        String data = "actualizado_I";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    } else {
                        formularioProcesoDb.setFopEstado("A");
                        formularioProcesoService.cambiarEstadoFormularioProceso(formularioProcesoDb);
                        String data = "actualizado_A";
                        return new ResponseEntity<>(data, HttpStatus.OK);
                    }

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
        return ResponseEntity.ok("Opcion no encontrada");

    }
}