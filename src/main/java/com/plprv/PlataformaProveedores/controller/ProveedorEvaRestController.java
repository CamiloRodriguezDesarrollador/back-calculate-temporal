package com.plprv.PlataformaProveedores.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plprv.PlataformaProveedores.entity.DocumentosProveedor;
import com.plprv.PlataformaProveedores.entity.PeriodoEvaluacion;
import com.plprv.PlataformaProveedores.entity.Proveedor;
import com.plprv.PlataformaProveedores.entity.ProveedorEva;
import com.plprv.PlataformaProveedores.service.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProveedorEvaRestController {
    @Autowired
    private IProveedorEvaServices proveedorEvaService;

    private final EmailService emailService;
    @Autowired
    private IPeriodoEvaluacionServices periodoEvaluacionService;

    @Autowired
    private IProveedorServices proveedorService;

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;

    public ProveedorEvaRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/proveedorEva")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> obtenerProveedorEvas(){
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping("/proveedorEva")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector')")
    public ResponseEntity<?> opcionesPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        String opcion = (String) requestBody.get("opcion");
        String checkBoxEstado = (String) requestBody.get("checkBoxEstado");

        switch (opcion) {

            case "encontrarResultados" : {
                Integer perIdR = (Integer) requestBody.get("per_id");
                Integer prvIdR = (Integer) requestBody.get("prv_id");
                ProveedorEva miProveedorEva = proveedorEvaService.encontrarProveedorEvaPorPerId(perIdR, prvIdR,miIdEmppal);
                if (miProveedorEva != null) {
                    return new ResponseEntity<>(miProveedorEva.getPreResultado(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(0, HttpStatus.OK);
                }
            }
            case "informacionTotal" : {
                Integer numeroDePagina = (Integer) requestBody.get("numeroDePagina");
                Integer numeroElementosPorPagina = (Integer) requestBody.get("numeroElementosPorPagina");
                String texto = (String) requestBody.get("texto");
                Integer perId = (Integer) requestBody.get("perId");
                List<ProveedorEva> proveedorEvasTodosDb = proveedorEvaService.encontrarProveedorEvaFiltroPaginas(checkBoxEstado, texto,
                        numeroDePagina, numeroElementosPorPagina, perId,miIdEmppal);
                if (proveedorEvasTodosDb != null) {
                    return new ResponseEntity<>(proveedorEvasTodosDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cantidadDePaginas" : {
                String textoC = (String) requestBody.get("texto");
                Integer perIdC = (Integer) requestBody.get("perId");
                Integer proveedorEvaTodosDbC = proveedorEvaService.cantidadPaginasProveedorEva(checkBoxEstado, textoC, perIdC,miIdEmppal);
                if (proveedorEvaTodosDbC != null) {
                    return new ResponseEntity<>(proveedorEvaTodosDbC, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "obtenerId" : {
                Integer preId = (Integer) requestBody.get("fod_id");
                ProveedorEva proveedorEvasDbI = proveedorEvaService.encontrarProveedorEvaPorId(preId, null,miIdEmppal);
                if (proveedorEvasDbI != null) {
                    return new ResponseEntity<>(proveedorEvasDbI, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "proveedorEvaCheck" : {
                Integer perIdD = (Integer) requestBody.get("per_id");
                List<ProveedorEva> proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorFormulario(perIdD, "A",miIdEmppal);
                if (proveedorEvasDb != null) {
                    return new ResponseEntity<>(proveedorEvasDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            case "cambiarEstadoPeriodo" : {
                Integer perIdE = (Integer) requestBody.get("per_id");
                Integer prvIdE = (Integer) requestBody.get("prv_id");
                Integer idEmppalE = (Integer) requestBody.get("id_emppal");
                ProveedorEva proveedorEvaE = proveedorEvaService.encontrarProveedorEvaPorPerId(perIdE, prvIdE,miIdEmppal);
                if (proveedorEvaE != null) {
                    String estado = proveedorEvaE.getPreEstado();
                    if (estado.equals("NA")) {
                        if (proveedorEvaE.getPreResultado() == 0) {
                            proveedorEvaE.setPreEstado("NI");
                        } else {
                            proveedorEvaE.setPreEstado("I");
                        }
                        proveedorEvaService.actualizarProveedorEva(proveedorEvaE);
                    }
                    if (estado.equals("NI")) {
                        proveedorEvaE.setPreEstado("NA");
                        proveedorEvaService.actualizarProveedorEva(proveedorEvaE);
                    }
                    if (estado.equals("I")) {
                        proveedorEvaE.setPreEstado("NA");
                        proveedorEvaService.actualizarProveedorEva(proveedorEvaE);
                    }
                    return new ResponseEntity<>(proveedorEvaE, HttpStatus.OK);
                } else {
                    ProveedorEva miProveedorEvaE = new ProveedorEva();
                    miProveedorEvaE.setIdEmppal(idEmppalE);
                    miProveedorEvaE.setPerId(perIdE);
                    miProveedorEvaE.setPrvId(prvIdE);
                    miProveedorEvaE.setPreResultado(0);
                    miProveedorEvaE.setPreContinua("SI");
                    miProveedorEvaE.setPreObservacion("");
                    miProveedorEvaE.setPreEstado("NI");
                    Date fechaActual = new Date();
                    Set<Date> conjunctrecast = new HashSet<>();
                    conjunctrecast.add(fechaActual);
                    miProveedorEvaE.setAudFecha(fechaActual);
                    miProveedorEvaE.setAudUsuario(miAud);
                    try {
                        proveedorEvaService.crearProveedorEva(miProveedorEvaE);
                    } catch (DataIntegrityViolationException e) {
                        if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                            String data = "dato_existente";
                            return new ResponseEntity<>(data, HttpStatus.OK);
                        } else {
                            String data = "error_sql";
                            return new ResponseEntity<>(data, HttpStatus.OK);
                        }
                    }
                    return new ResponseEntity<>(miProveedorEvaE, HttpStatus.OK);
                }
            }
            case "enviarCorreoNoIniciado" : {
                List<String> correosProveedor = new ArrayList<>();
                Integer perIdEn = (Integer) requestBody.get("per_id");
                List<ProveedorEva> periodoEvaluacions = proveedorEvaService.encontrarProveedorEvaPorFormulario(perIdEn, "NI",miIdEmppal);
                for (ProveedorEva evaluacion : periodoEvaluacions) {
                    System.out.println("entra como no iniciado: " + evaluacion.getPrvId() );
                    Proveedor proveedor = proveedorService.encontrarProveedoresPorId(evaluacion.getPrvId(), "A",miIdEmppal);
                    String coreeoProveedor = proveedor.getPrvCorreo();
                    correosProveedor.add(coreeoProveedor);
                }
                PeriodoEvaluacion miPeriodo = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perIdEn, null,miIdEmppal);
                String nombrePeriodo = miPeriodo.getPerNombre() + " Fechas : " + miPeriodo.getPerFechaEvaluacion();
                emailService.sendListEmailNoIniciado(correosProveedor, nombrePeriodo);
                return new ResponseEntity<>(correosProveedor, HttpStatus.OK);
            }
            case "enviarCorreoIncompleto" : {
                List<String> correosProveedorD = new ArrayList<>();
                Integer perIdEnD = (Integer) requestBody.get("per_id");
                List<ProveedorEva> periodoEvaluacionsD = proveedorEvaService.encontrarProveedorEvaPorFormulario(perIdEnD, "I",miIdEmppal);
                for (ProveedorEva evaluacion : periodoEvaluacionsD) {
                    Proveedor proveedor = proveedorService.encontrarProveedoresPorId(evaluacion.getPrvId(), "A",miIdEmppal);
                    String coreeoProveedor = proveedor.getPrvCorreo();
                    correosProveedorD.add(coreeoProveedor);
                }
                PeriodoEvaluacion miPeriodoD = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perIdEnD, null,miIdEmppal);
                String nombrePeriodoD = miPeriodoD.getPerNombre() + " Fechas : " + miPeriodoD.getPerFechaEvaluacion();
                emailService.sendListEmailIncompleto(correosProveedorD, nombrePeriodoD);
                return ResponseEntity.ok("No hay proveedores recalcular");
            }
            case "calcularEvaluacionPorcentaje" : {
                String estado = "I";
                Integer perIdCa = (Integer) requestBody.get("per_id");
                List<DocumentosProveedor> proveedoresCacular = proveedorEvaService.encontrarProveedoresEstados(perIdCa,miIdEmppal);
                PeriodoEvaluacion periodoEvaluacionsDbI = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perIdCa, null,miIdEmppal);
                if ((periodoEvaluacionsDbI.getPerTipo().equals("Evaluacion")))
                    return ResponseEntity.ok("Periodo Evaluacion no se evalua");
                if (proveedoresCacular == null) return ResponseEntity.ok("No hay proveedores recalcular");
                for (DocumentosProveedor proveedor : proveedoresCacular) {
                    Long cantidadRequerida = proveedorEvaService.encontrarCantidadRequerida(perIdCa, proveedor.getProId(), proveedor.getSprId(), proveedor.getCrtId()
                            , proveedor.getTdcTd(),miIdEmppal);
                    double porcentajeNuevo = Math.round((proveedor.getCantidad().doubleValue() / cantidadRequerida.doubleValue()) * 100.0);
                    ProveedorEva proveedorActualizar = proveedorEvaService.encontrarProveedorEvaPorPerId(perIdCa, proveedor.getPrvId(),miIdEmppal);

                    if (porcentajeNuevo >= 100) {
                        porcentajeNuevo = 100;
                        estado = "C";
                    }
                    proveedorActualizar.setPreEstado(estado);
                    proveedorActualizar.setPreResultado((int) porcentajeNuevo);
                    proveedorEvaService.actualizarProveedorEva(proveedorActualizar);
                }
                return ResponseEntity.ok("actualizado");
            }
            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }

    @PostMapping("/proveedorEvaL")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> opcionesPostL(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String miAud = obtenerUsuarioAud.obtnerUsuarioToken(token);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);
        ObjectMapper objectMapper = new ObjectMapper();
        String datosJson = objectMapper.writeValueAsString(requestBody.get("datos"));
        JsonNode jsonNode = objectMapper.readTree(datosJson);
        String opcion = (String) requestBody.get("opcion");

        switch (opcion) {
            case "guardarFormulario" : {
                try {
                    int idEmppal = jsonNode.get("id_emppal").asInt();
                    int prvId = jsonNode.get("prv_id").asInt();
                    int perId = jsonNode.get("per_id").asInt();
                    int preResultado = jsonNode.get("pre_resultado").asInt();
                    String preObservacion = jsonNode.get("pre_observacion").asText().trim();
                    String preContinua = jsonNode.get("pre_continua").asText().trim();
                    String estado = "NI";
                    if (preResultado == 100) estado = "C";
                    if (preResultado > 0 && preResultado < 100) estado = "I";

                    try {
                        PeriodoEvaluacion periodoEvaluacions = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perId, null,miIdEmppal);
                        String tipo = periodoEvaluacions.getPerTipo();
                        if (tipo.equals("evaluacion")) estado = "C";

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(perId, prvId,miIdEmppal);
                    if (proveedorEvasDb == null) {
                        ProveedorEva miProveedorEva = new ProveedorEva();
                        miProveedorEva.setIdEmppal(idEmppal);
                        miProveedorEva.setPerId(perId);
                        miProveedorEva.setPrvId(prvId);
                        miProveedorEva.setPreResultado(preResultado);
                        miProveedorEva.setPreObservacion(preObservacion);
                        miProveedorEva.setPreContinua(preContinua);
                        miProveedorEva.setPreEstado(estado);
                        Date fechaActual = new Date();
                        Set<Date> conjunctrecast = new HashSet<>();
                        conjunctrecast.add(fechaActual);
                        miProveedorEva.setAudFecha(fechaActual);
                        miProveedorEva.setAudUsuario(miAud);

                        try {
                            proveedorEvaService.crearProveedorEva(miProveedorEva);
                        } catch (DataIntegrityViolationException e) {
                            if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1) {
                                String data = "dato_existente";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            } else {
                                String data = "error_sql";
                                return new ResponseEntity<>(data, HttpStatus.OK);
                            }
                        }
                        return new ResponseEntity<>(miProveedorEva, HttpStatus.OK);
                    } else {
                        proveedorEvasDb.setPreResultado(preResultado);
                        proveedorEvasDb.setPreObservacion(preObservacion);
                        proveedorEvasDb.setPreContinua(preContinua);
                        proveedorEvasDb.setPreEstado(estado);
                        proveedorEvaService.actualizarProveedorEva(proveedorEvasDb);
                        return new ResponseEntity<>(proveedorEvasDb, HttpStatus.OK);
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
            case "proveedorPorTokenFormulario" : {
                Integer prvId = (Integer) requestBody.get("prv_id");
                List<ProveedorEva> proveedorDb = proveedorEvaService.encontrarProveedorEvaPorPrvId(prvId,miIdEmppal);
                if (proveedorDb != null) {
                    return new ResponseEntity<>(proveedorDb, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
            default : {
                return ResponseEntity.ok("Opcion no encontrada");
            }
        }

    }


}
