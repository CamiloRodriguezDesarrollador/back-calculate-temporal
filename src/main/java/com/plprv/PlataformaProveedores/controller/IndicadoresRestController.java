package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.service.IIndicadoresServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class IndicadoresRestController {
    @Autowired
    private IIndicadoresServices indicadoresService;

    @PostMapping("/indicadores")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);

        Integer perId = 0;
        Integer crtId = 0;
        Integer proId = 0;

        String opcion = (String) requestBody.get("opcion");

        if ( jsonNode.get("per_id") != null ) perId = jsonNode.get("per_id").asInt();
        if ( jsonNode.get("crt_id") != null ) crtId = jsonNode.get("crt_id").asInt();
        if ( jsonNode.get("pro_id") != null ) proId = jsonNode.get("pro_id").asInt();


        switch (opcion){
            case "cantidadProveedoresTotal":
                Integer cantidadTotalProveedores = indicadoresService.cantidadProveedoresTotal();
                return new ResponseEntity<>(cantidadTotalProveedores, HttpStatus.OK);
            case "cantidadPeriodossTotal":
                Integer cantidadTotalPeriodos = indicadoresService.cantidadPeriodosTotal();
                return new ResponseEntity<>(cantidadTotalPeriodos, HttpStatus.OK);
            case "cantidadProcesosTotal":
                Integer cantidadProcesosTotal = indicadoresService.cantidadProcesosTotal();
                return new ResponseEntity<>(cantidadProcesosTotal, HttpStatus.OK);
            case "obtenerTodaInformacionProveedorEva":
                List<Object>  informacionTotal = indicadoresService.obtenerTodaInformacionProveedorEva(crtId,perId,proId);
                return new ResponseEntity<>(informacionTotal, HttpStatus.OK);
            case "informacionTotalGrafico":
                List<Object> informacionTotalGrafico = indicadoresService.obtenerTodaInformacionTabla(crtId,perId,proId);
                return new ResponseEntity<>(informacionTotalGrafico, HttpStatus.OK);
            case "cantidadProveedoresFiltro":
                Integer cantidadProveedoresFiltro = indicadoresService.cantidadProveedoreFiltroTabla(crtId,perId,proId);
                return new ResponseEntity<>(cantidadProveedoresFiltro, HttpStatus.OK);
            case "cantidadProcesosFiltro":
                List<Object> informacionProcesosGrafico = indicadoresService.obtenerProcesosFiltroTabla(crtId,perId,proId);
                return new ResponseEntity<>(informacionProcesosGrafico, HttpStatus.OK);
            case "cantidadDocumentosFiltro":
                List<Object> informacionDocumentosFiltro = indicadoresService.obtenerEstadoDocimentos(crtId,perId,proId);
                return new ResponseEntity<>(informacionDocumentosFiltro, HttpStatus.OK);
            case "calcularPorcentaje":
                Double procentaje = indicadoresService.porcentajeAvance(crtId,perId,proId);
                return new ResponseEntity<>(procentaje, HttpStatus.OK);
            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }
}