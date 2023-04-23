package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import com.plprv.PlataformaProveedores.entity.Proceso;
import com.plprv.PlataformaProveedores.service.IFormularioProcesoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FormularioProcesoRestController {
    @Autowired
    private IFormularioProcesoServices formularioProcesoService;

    @GetMapping("/formularioProceso")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerFormularioProcesos() {
        List<FormularioProceso> formularioProcesosDb = formularioProcesoService.encontrarFormularioProceso();
        if (formularioProcesosDb != null) {
            return new ResponseEntity<>(formularioProcesosDb, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
    }

    @PostMapping("/formularioProceso")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("form"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");

        switch (opcion) {
            case "cambiarEstado":
                try {
                    String tdcTd = jsonNode.get("tdc_td").asText().trim();
                    Integer forId = jsonNode.get("for_id").asInt();
                    Integer perId = jsonNode.get("per_id").asInt();
                    Integer fodId = jsonNode.get("fod_id").asInt();
                    Integer crtId = jsonNode.get("crt_id").asInt();
                    Integer proId = jsonNode.get("pro_id").asInt();
                    Integer sprId = jsonNode.get("spr_id").asInt();
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();
                    FormularioProceso formularioProcesoDb = formularioProcesoService.encontrarFormularioPorParametros(forId, fodId, proId, sprId, perId, crtId, tdcTd, idEmppal);
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
                        miFormularioProceso.setAudUsuario(audUsuario);
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
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }
}