package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.Documentos;
import com.plprv.PlataformaProveedores.entity.FormularioProceso;
import com.plprv.PlataformaProveedores.entity.ProveedorDoc;
import com.plprv.PlataformaProveedores.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    @GetMapping("/proveedorDoc")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> obtenerProveedorDocs(){
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping("/proveedorDoc")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);

        switch (opcion) {
            case "informacionTabla" -> {
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
                proveedorDocService.actualizarTodosEstado(prvIdD, crtId, forId, texto, prvNd, prvNombre, perId, proId, sprId, prdEstadoDocumental, tipoD,miIdEmppal);
                List<Object> proveedorTabla = proveedorDocService.encontrarTablaDocumental(prvIdD, crtId, forId, texto, prvNd,
                        prvNombre, perId, proId, sprId, prdEstadoDocumental, tipoD, numeroDePagina, numeroElementosPorPagina,miIdEmppal);
                if (proveedorTabla != null) {
                    return new ResponseEntity<>(proveedorTabla, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" -> {
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
                Integer proveedorTablaC = proveedorDocService.cantidadPaginasProveedorDoc(prvIdP, crtIdP, forIdP, textoP
                        , prvNdP, prvNombreP, perIdP, proIdP, sprIdP, prdEstadoDocumentalP, tipoP,miIdEmppal);
                if (proveedorTablaC != null) {
                    return new ResponseEntity<>(proveedorTablaC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            case "informacionTablaTodo" -> {
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
                List<Documentos> proveedorTablaT = proveedorDocService.encontrarTablaDocumentalTodo(prvIdDT, crtIdT, forIdT, textoT,
                        prvNdT, prvNombreT, perIdT, proIdT, sprIdT, prdEstadoDocumentalT, tipoT,miIdEmppal);
                if (proveedorTablaT != null) {
                    return new ResponseEntity<>(proveedorTablaT, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            case "calcularDocumentacion" -> {
                Integer fodId = jsonNode.get("fod_id").asInt();
                String estadoNuevo = "A";
                List<FormularioProceso> formularios = formularioProcesoService.encontrarFormulariosPorDetalle(fodId,miIdEmppal);
                if (formularios == null) return null;
                String vigenciaFormularioDetalle = formularioDetalleService.encontrarFormularioDetallesPorId(fodId, null,miIdEmppal).getFodVigencia();
                String tipoDetalle = formularioDetalleService.encontrarFormularioDetallesPorId(fodId, null,miIdEmppal).getFodTipo();
                if (vigenciaFormularioDetalle == null) return null;
                if (!tipoDetalle.equals("file")) return null;
                for (FormularioProceso form : formularios) {
                    List<ProveedorDoc> proveedorDoc = proveedorDocService.encontrarProveedoresPorFopId(form.getFopId(),miIdEmppal);
                    for (ProveedorDoc prov : proveedorDoc) {
                        if (prov.getPrdData() == null || prov.getPrdData().equals("")) continue;
                        long diasVigencia = 0L;
                        LocalDate fecha;
                        LocalDate hoy;
                        try {
                            switch (vigenciaFormularioDetalle) {
                                case "1" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 365 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "2" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 730 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "3" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1095 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "4" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1460 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "5" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 1825 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "10" -> {
                                    fecha = LocalDate.parse(prov.getPrdFechaDocumento(), DateTimeFormatter.ISO_LOCAL_DATE);
                                    hoy = LocalDate.now();
                                    diasVigencia = 3650 - ChronoUnit.DAYS.between(fecha, hoy);
                                }
                                case "NA" -> diasVigencia = 91;
                                default -> {
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        if (diasVigencia >= 90) estadoNuevo = "A";
                        if (diasVigencia >= 30 && diasVigencia <= 90) estadoNuevo = "A-3";
                        if (diasVigencia > 0 && diasVigencia < 30) estadoNuevo = "A-1";
                        if (diasVigencia <= 0) estadoNuevo = "V";

                        ProveedorDoc miProveedorActualizar = proveedorDocService.encontrarProveedorDocsPorId(prov.getPrdId(),miIdEmppal);
                        System.out.println("miProveedorActualizar" + miProveedorActualizar.getPrdFechaDocumento());

                        miProveedorActualizar.setPrdEstadoDocumental(estadoNuevo);
                        proveedorDocService.actualizarProveedorDoc(miProveedorActualizar);
                    }
                }
                return new ResponseEntity<>("actualizado", HttpStatus.OK);
            }
            case "informeGeneral" -> {
                Integer prvIdDG = jsonNode.get("prv_id").asInt();
                Integer crtIdG = jsonNode.get("crt_id").asInt();
                Integer perIdG = jsonNode.get("per_id").asInt();
                Integer proIdG = jsonNode.get("pro_id").asInt();
                String prvNdG = jsonNode.get("prv_nd").asText().trim();
                String prvNombreG = jsonNode.get("prv_nombre").asText().trim();
                Integer sprIdG = jsonNode.get("spr_id").asInt();
                String preEstadoG = jsonNode.get("pre_estado").asText().trim();
                List<Object> proveedorTablaG = proveedorDocService.descargarInformeGeneral(prvIdDG, crtIdG, prvNdG, prvNombreG, perIdG, proIdG,
                        sprIdG, preEstadoG,miIdEmppal);
                if (proveedorTablaG != null) {
                    return new ResponseEntity<>(proveedorTablaG, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            case "informacionTablaEvaluacion" -> {
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
                List<Object> proveedorTablaE = proveedorDocService.encontrarTablaEvaluacion(prvIdE, crtIdE, prvNdE, prvNombreE,
                        perIdE, proIdE, sprIdE, preEstadoE, numeroDePaginaE, numeroElementosPorPaginaE,miIdEmppal);
                if (proveedorTablaE != null) {
                    return new ResponseEntity<>(proveedorTablaE, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            case "cantidadDePaginasEvaluacion" -> {
                Integer prvIdC = jsonNode.get("prv_id").asInt();
                Integer crtIdC = jsonNode.get("crt_id").asInt();
                Integer perIdC = jsonNode.get("per_id").asInt();
                Integer proIdC = jsonNode.get("pro_id").asInt();
                String prvNdC = jsonNode.get("prv_nd").asText().trim();
                String prvNombreC = jsonNode.get("prv_nombre").asText().trim();
                Integer sprIdC = jsonNode.get("spr_id").asInt();
                String preEstadoC = jsonNode.get("pre_estado").asText().trim();
                Integer proveedorTablaCE = proveedorDocService.cantidadPaginasProveedorEva(prvIdC, crtIdC, prvNdC, prvNombreC, perIdC, proIdC, sprIdC, preEstadoC,miIdEmppal);
                if (proveedorTablaCE != null) {
                    return new ResponseEntity<>(proveedorTablaCE, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            default -> {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

    @PostMapping("/proveedorDocL")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");
        String tipo = (String) requestBody.get("tipo");
        String miEstado = "A";
        String vigencia = "NA";

        switch (opcion) {
            case "guardarFormulario" -> {
                try {
                    Integer idEmppal = jsonNode.get("id_emppal").asInt();
                    Integer fopId = jsonNode.get("fop_id").asInt();
                    String prdData = jsonNode.get("prd_data").asText().trim();
                    String prdObservacion = jsonNode.get("prd_observacion").asText().trim();
                    String prdFechaDocumento = jsonNode.get("prd_fecha_documento").asText().trim();

                    if (!regexService.isId(idEmppal) || !regexService.isSelectNumber(fopId) || !regexService.isData(prdData) ||
                            !regexService.isObservacion(prdObservacion)
                    ) return new ResponseEntity<>("campos incorrectos", HttpStatus.OK);

                    FormularioProceso miFormulario = formularioProcesoService.encontrarFormulariosPorId(fopId,miIdEmppal);
                    if (miFormulario != null) {
                        Integer fodId = miFormulario.getFodId();
                        String miVigencia = formularioDetalleService.encontrarFormularioDetallesPorId(fodId, null,miIdEmppal).getFodVigencia();
                        if (miVigencia != null) {
                            vigencia = miVigencia;
                        }
                    }
                    long diasVigencia = 0L;
                    LocalDate fecha;
                    LocalDate hoy;

                    try {
                        switch (vigencia) {
                            case "1" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 365 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            case "2" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 730 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            case "3" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1095 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            case "4" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1460 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            case "5" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 1825 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            case "10" -> {
                                fecha = LocalDate.parse(prdFechaDocumento, DateTimeFormatter.ISO_LOCAL_DATE);
                                hoy = LocalDate.now();
                                diasVigencia = 3650 - ChronoUnit.DAYS.between(fecha, hoy);
                            }
                            default -> {
                            }
                        }
                    } catch (Exception ignored) {
                    }

                    int prvId = jsonNode.get("prv_id").asInt();
                    String prdEstadoDocumental = jsonNode.get("prd_estado_documental").asText().trim();
                    ProveedorDoc proveedorDocsDb = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId,miIdEmppal);

                    if (diasVigencia >= 90) miEstado = "A";
                    if (diasVigencia >= 30 && diasVigencia <= 90) miEstado = "A-3";
                    if (diasVigencia > 0 && diasVigencia < 30) miEstado = "A-1";
                    if (diasVigencia <= 0) miEstado = "V";
                    if (!prdData.equals("") && !tipo.equals("file")) miEstado = "A";
                    if (prdData.equals("") && !tipo.equals("file")) miEstado = "P";

                    if (proveedorDocsDb == null) {

                        if (prdData.equals("") && tipo.equals("file")) miEstado = "P";

                        ProveedorDoc miProveedorDoc = new ProveedorDoc();
                        miProveedorDoc.setIdEmppal(idEmppal);
                        miProveedorDoc.setFopId(fopId);
                        if (!tipo.equals("file")) {
                            miProveedorDoc.setPrdData(prdData);
                        }
                        miProveedorDoc.setPrdEstadoDocumental(miEstado);
                        miProveedorDoc.setPrdFechaDocumento(prdFechaDocumento);
                        miProveedorDoc.setPrvId(prvId);
                        miProveedorDoc.setPrdObservacion(prdObservacion);
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miProveedorDoc.setAudFecha(fechaActual);
                        miProveedorDoc.setAudUsuario(miAud);
                        try {
                            proveedorDocService.crearProveedorDoc(miProveedorDoc);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miProveedorDoc, HttpStatus.OK);
                    } else {
                        if (!tipo.equals("file")) {
                            proveedorDocsDb.setPrdData(prdData);
                        }
                        if (proveedorDocsDb.getPrdData() == null || proveedorDocsDb.getPrdData().equals("") && tipo.equals("file"))
                            miEstado = "P";
                        proveedorDocsDb.setPrdEstadoDocumental(miEstado);
                        proveedorDocsDb.setPrdFechaDocumento(prdFechaDocumento);
                        proveedorDocsDb.setPrdObservacion(prdObservacion);
                        proveedorDocService.actualizarProveedorDoc(proveedorDocsDb);
                        return new ResponseEntity<>(proveedorDocsDb, HttpStatus.OK);
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
            case "proveedorPorTokenFormulario" -> {
                Integer prvId = (Integer) requestBody.get("prv_id");
                List<ProveedorDoc> proveedorDb = proveedorDocService.encontrarProveedorDocsPorPrvId(prvId,miIdEmppal);
                if (proveedorDb != null) {
                    return new ResponseEntity<>(proveedorDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("sin_datos", HttpStatus.OK);
                }
            }
            default -> {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

}
