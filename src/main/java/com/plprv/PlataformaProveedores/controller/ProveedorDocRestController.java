package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plprv.PlataformaProveedores.entity.FormularioDetalle;
import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import com.plprv.PlataformaProveedores.service.IFormularioDetalleServices;
import com.plprv.PlataformaProveedores.service.IFormularioProcesoServices;
import com.plprv.PlataformaProveedores.service.IProveedorDocServices;
import com.plprv.PlataformaProveedores.service.IRegexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProveedorDocRestController {
    @Autowired
    private IProveedorDocServices proveedorDocService;
    @Autowired
    private IFormularioProcesoServices formularioProcesoService;
    @Autowired
    private IFormularioDetalleServices formularioDetalleService;
    @Autowired
    private IRegexService regexService;

    @GetMapping("/proveedorDoc")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> obtenerProveedorDocs(){
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping("/proveedorDoc")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String tipo = (String) requestBody.get("tipo");
        String miEstado = "A";
        String vigencia = "NA";

        switch (opcion){
            case "guardarFormulario":
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    Integer fopId = jsonNode.get("fop_id").asInt();
                    String prdData = jsonNode.get("prd_data").asText().trim();
                    String prdObservacion = jsonNode.get("prd_observacion").asText().trim();
                    String prdFechaDocumento = jsonNode.get("prd_fecha_documento").asText().trim();
                    String audUsuario = jsonNode.get("aud_usuario").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isSelectNumber(fopId) || !regexService.isData(prdData) ||
                            !regexService.isObservacion(prdObservacion)  || !regexService.isTextNormal(audUsuario)
                    )   return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    FormularioProceso miFormulario = formularioProcesoService.encontrarFormulariosPorId(fopId);
                    if (miFormulario != null) {
                        Integer fodId = miFormulario.getFodId();
                        String miVigencia = formularioDetalleService.encontrarFormularioDetallesPorId(fodId,null).getFodVigencia();
                        if (miVigencia!=null){
                            vigencia = miVigencia;
                        }
                    }
                    long diasVigencia = 0L;
                    LocalDate fecha;
                    LocalDate hoy;

                    try {
                        switch (vigencia) {
                            case "1":
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 365 - ChronoUnit.DAYS.between(fecha, hoy);
                                break;
                            case "2":
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 730 - ChronoUnit.DAYS.between(fecha, hoy);
                                break;

                            case "3":
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1095 - ChronoUnit.DAYS.between(fecha, hoy);
                                break;

                            case "4":
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1460 - ChronoUnit.DAYS.between(fecha, hoy);
                                break;

                            case "5":
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1825 - ChronoUnit.DAYS.between(fecha, hoy);
                                break;

                            default:
//                                miEstado = "A";
//                                diasVigencia = 0L;
                        }
                    }
                    catch (Exception e) {
//                        throw new RuntimeException(e);
                    }
                    if (diasVigencia>=90) miEstado = "A";
                    if (diasVigencia>=30 && diasVigencia<=90) miEstado = "A-3";
                    if (diasVigencia>0 && diasVigencia<30) miEstado = "A-1";
                    if (diasVigencia<=0) miEstado = "V";
                    if ((prdData.equals("") || prdData == null) && !tipo.equals("file") ) miEstado = "P";
                    if (!(prdData.equals("") && prdData != null) && !tipo.equals("file") ) miEstado = "A";


                    Integer prvId = jsonNode.get("prv_id").asInt();
                    String prdEstadoDocumental = jsonNode.get("prd_estado_documental").asText().trim();
                    ProveedorDoc proveedorDocsDb = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId);
                    if(proveedorDocsDb == null) {
                       ProveedorDoc miProveedorDoc = new ProveedorDoc();
                       miProveedorDoc.setIdEmppal(idEmppal);
                       miProveedorDoc.setFopId(fopId);
                       if (!tipo.equals("file")){
                           miProveedorDoc.setPrdData(prdData);
                       }
                       miProveedorDoc.setPrdEstadoDocumental(miEstado);
                       miProveedorDoc.setPrdFechaDocumento(prdFechaDocumento);
                       miProveedorDoc.setPrvId(prvId);
                       miProveedorDoc.setPrdObservacion(prdObservacion);
                       Date fechaActual = new Date();
                       Set<Date> conjuntoFechas = new HashSet<>();
                       conjuntoFechas.add(fechaActual);
                       miProveedorDoc.setAudFecha(fechaActual);
                       miProveedorDoc.setAudUsuario(audUsuario.toLowerCase());
                        try {
                            proveedorDocService.crearProveedorDoc(miProveedorDoc);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>( data, HttpStatus.OK);
                            }
                        }
                       return new ResponseEntity<>(miProveedorDoc, HttpStatus.OK);
                    }else{
                        if (!tipo.equals("file")){
                            proveedorDocsDb.setPrdData(prdData);
                        }
                        if (proveedorDocsDb.getPrdData() == null) miEstado = "P";
                        proveedorDocsDb.setPrdEstadoDocumental(miEstado);
                        proveedorDocsDb.setPrdFechaDocumento(prdFechaDocumento);
                        proveedorDocsDb.setPrdObservacion(prdObservacion);
                        proveedorDocService.actualizarProveedorDoc(proveedorDocsDb);
                        return  new ResponseEntity<>(proveedorDocsDb , HttpStatus.OK);
                    }
                }catch (Exception e){
                    if (e.getMessage().contains("null")){
                        String data = "campos_incompletos";
                        return new ResponseEntity<>( data, HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>( e.getMessage(), HttpStatus.OK);
                    }
                }
            case "proveedorPorTokenFormulario":
                Integer prvId = (Integer) requestBody.get("prv_id");
                List<ProveedorDoc> proveedorDb = proveedorDocService.encontrarProveedorDocsPorPrvId(prvId);
                if(proveedorDb!=null){
                    return  new ResponseEntity<>(proveedorDb , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }
            case "informacionTabla":
                Integer numeroDePagina = jsonNode.get("numeroDePagina").asInt();
                Integer numeroElementosPorPagina = jsonNode.get("numeroElementosPorPagina").asInt();
                Integer prvIdD = jsonNode.get("prv_id").asInt();
                Integer crtId = jsonNode.get("crt_id").asInt();
                Integer forId = jsonNode.get("for_id").asInt();
                String texto = jsonNode.get("texto").asText().trim();
                Integer perId = jsonNode.get("per_id").asInt();
                Integer proId = jsonNode.get("pro_id").asInt();
                String prvNd = jsonNode.get("prv_nd").asText().trim();
                String prvNombre = jsonNode.get("prv_nombre").asText().trim();
                String tipoD = jsonNode.get("tipo").asText().trim();
                Integer sprId = jsonNode.get("spr_id").asInt();
                String prdEstadoDocumental = jsonNode.get("prd_estadoDocumental").asText().trim();
                List<Object> proveedorTabla = proveedorDocService.encontrarTablaDocumental(prvIdD,crtId,forId,texto, prvNd,prvNombre, perId, proId, sprId, prdEstadoDocumental,tipoD , numeroDePagina, numeroElementosPorPagina);

                if(proveedorTabla!=null){
                    return  new ResponseEntity<>(proveedorTabla , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            case "cantidadDePaginas":
                Integer prvIdP = jsonNode.get("prv_id").asInt();
                Integer crtIdP = jsonNode.get("crt_id").asInt();
                Integer perIdP = jsonNode.get("per_id").asInt();
                Integer forIdP = jsonNode.get("for_id").asInt();
                String textoP = jsonNode.get("texto").asText().trim();
                Integer proIdP = jsonNode.get("pro_id").asInt();
                Integer sprIdP = jsonNode.get("spr_id").asInt();
                String tipoP = jsonNode.get("tipo").asText().trim();
                String prvNdP = jsonNode.get("prv_nd").asText().trim();
                String prvNombreP = jsonNode.get("prv_nombre").asText().trim();
                String prdEstadoDocumentalP = jsonNode.get("prd_estadoDocumental").asText().trim();
                Integer proveedorTablaC = proveedorDocService.cantidadPaginasProveedorDoc(prvIdP, crtIdP, forIdP,textoP,prvNdP,prvNombreP, perIdP, proIdP, sprIdP, prdEstadoDocumentalP, tipoP);
                if(proveedorTablaC!=null){
                    return  new ResponseEntity<>(proveedorTablaC , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            case "informacionTablaTodo":
                Integer prvIdDT = jsonNode.get("prv_id").asInt();
                Integer crtIdT = jsonNode.get("crt_id").asInt();
                Integer forIdT = jsonNode.get("for_id").asInt();
                String textoT = jsonNode.get("texto").asText().trim();
                Integer perIdT = jsonNode.get("per_id").asInt();
                Integer proIdT = jsonNode.get("pro_id").asInt();
                String prvNdT = jsonNode.get("prv_nd").asText().trim();
                String prvNombreT = jsonNode.get("prv_nombre").asText().trim();
                String tipoT = jsonNode.get("tipo").asText().trim();
                Integer sprIdT = jsonNode.get("spr_id").asInt();
                String prdEstadoDocumentalT = jsonNode.get("prd_estadoDocumental").asText().trim();
                List<Object> proveedorTablaT = proveedorDocService.encontrarTablaDocumentalTodo(prvIdDT,crtIdT,forIdT,textoT, prvNdT,prvNombreT, perIdT, proIdT, sprIdT, prdEstadoDocumentalT,tipoT);

                if(proveedorTablaT!=null){
                    return  new ResponseEntity<>(proveedorTablaT , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            case "calcularDocumentacion":
                Integer fodId = jsonNode.get("fod_id").asInt();
                String estadoNuevo = "A";
                List<FormularioProceso> formularios = formularioProcesoService.encontrarFormulariosPorDetalle(fodId);
                if (formularios == null) return null;
                String vigenciaFormularioDetalle = formularioDetalleService.encontrarFormularioDetallesPorId(fodId,null ).getFodVigencia();
                String tipoDetalle = formularioDetalleService.encontrarFormularioDetallesPorId(fodId,null).getFodTipo();
                if (vigenciaFormularioDetalle == null) return null;
                if (!tipoDetalle.equals("file")) return null;

                for (FormularioProceso form : formularios) {
                    List<ProveedorDoc> proveedorDoc = proveedorDocService.encontrarProveedoresPorFopId(form.getFopId());
                    for (ProveedorDoc prov : proveedorDoc){
                        long diasVigencia = 0L;
                        LocalDate fecha;
                        LocalDate hoy;
                        try {
                            switch (vigenciaFormularioDetalle) {
                                case "1":
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 365 - ChronoUnit.DAYS.between(fecha, hoy);
                                    break;
                                case "2":
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 730 - ChronoUnit.DAYS.between(fecha, hoy);
                                    break;

                                case "3":
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1095 - ChronoUnit.DAYS.between(fecha, hoy);
                                    break;

                                case "4":
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1460 - ChronoUnit.DAYS.between(fecha, hoy);
                                    break;

                                case "5":
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_DATE.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1825 - ChronoUnit.DAYS.between(fecha, hoy);
                                    break;

                                case "NA":
                                    diasVigencia = 91;
                                    break;

                                default:
                            }
                        }
                        catch (Exception e) {
                        }
                        if (diasVigencia>=90) estadoNuevo = "A";
                        if (diasVigencia>=30 && diasVigencia<=90) estadoNuevo = "A-3";
                        if (diasVigencia>0 && diasVigencia<30) estadoNuevo = "A-1";
                        if (diasVigencia<=0) estadoNuevo = "V";

                        ProveedorDoc miProveedorActualizar = proveedorDocService.encontrarProveedorDocsPorId(prov.getPrdId());
                        System.out.println("miProveedorActualizar"+miProveedorActualizar.getPrdFechaDocumento());

                        if (miProveedorActualizar == null) return null;
                        miProveedorActualizar.setPrdEstadoDocumental(estadoNuevo);
                        proveedorDocService.actualizarProveedorDoc(miProveedorActualizar);
                    }
                }


                return new ResponseEntity<>("actualizado",HttpStatus.OK);

            case "informeGeneral":
                Integer prvIdDG = jsonNode.get("prv_id").asInt();
                Integer crtIdG = jsonNode.get("crt_id").asInt();
                Integer perIdG = jsonNode.get("per_id").asInt();
                Integer proIdG = jsonNode.get("pro_id").asInt();
                String prvNdG = jsonNode.get("prv_nd").asText().trim();
                String prvNombreG = jsonNode.get("prv_nombre").asText().trim();
                Integer sprIdG = jsonNode.get("spr_id").asInt();
                String preEstadoG = jsonNode.get("pre_estado").asText().trim();

                List<Object> proveedorTablaG = proveedorDocService.descargarInformeGeneral(prvIdDG,crtIdG, prvNdG, prvNombreG , perIdG, proIdG, sprIdG, preEstadoG);
                if(proveedorTablaG!=null){
                    return  new ResponseEntity<>(proveedorTablaG , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            case "informacionTablaEvaluacion":
                Integer numeroDePaginaE = jsonNode.get("numeroDePagina").asInt();
                Integer numeroElementosPorPaginaE = jsonNode.get("numeroElementosPorPagina").asInt();
                Integer prvIdE = jsonNode.get("prv_id").asInt();
                Integer crtIdE = jsonNode.get("crt_id").asInt();
                Integer perIdE = jsonNode.get("per_id").asInt();
                Integer proIdE = jsonNode.get("pro_id").asInt();
                String prvNdE = jsonNode.get("prv_nd").asText().trim();
                String prvNombreE = jsonNode.get("prv_nombre").asText().trim();
                Integer sprIdE = jsonNode.get("spr_id").asInt();
                String preEstadoE = jsonNode.get("pre_estado").asText().trim();

                 List<Object> proveedorTablaE = proveedorDocService.encontrarTablaEvaluacion(prvIdE,crtIdE, prvNdE, prvNombreE , perIdE, proIdE, sprIdE, preEstadoE, numeroDePaginaE, numeroElementosPorPaginaE);

                if(proveedorTablaE!=null){
                    return  new ResponseEntity<>(proveedorTablaE , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            case "cantidadDePaginasEvaluacion":
                Integer prvIdC = jsonNode.get("prv_id").asInt();
                Integer crtIdC = jsonNode.get("crt_id").asInt();
                Integer perIdC = jsonNode.get("per_id").asInt();
                Integer proIdC = jsonNode.get("pro_id").asInt();
                String prvNdC= jsonNode.get("prv_nd").asText().trim();
                String prvNombreC = jsonNode.get("prv_nombre").asText().trim();
                Integer sprIdC = jsonNode.get("spr_id").asInt();
                String preEstadoC = jsonNode.get("pre_estado").asText().trim();
                Integer proveedorTablaCE = proveedorDocService.cantidadPaginasProveedorEva(prvIdC,crtIdC, prvNdC, prvNombreC , perIdC, proIdC, sprIdC, preEstadoC);
                if(proveedorTablaCE!=null){
                    return  new ResponseEntity<>(proveedorTablaCE , HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("sin_datos",HttpStatus.OK);
                }

            default:
                return ResponseEntity.ok("Opcion no encontrada");
        }

    }

}
