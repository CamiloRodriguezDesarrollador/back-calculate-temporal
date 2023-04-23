package com.plprv.PlataformaProveedores.controller;

import com.google.api.services.drive.model.FileList;
import com.plprv.PlataformaProveedores.entity.*;
import com.plprv.PlataformaProveedores.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.auth.oauth2.GoogleCredentials;


@RestController
@RequestMapping("/api")
public class ProveedorDocArchivoRestController {

    @Autowired
    private GoogleCredentials googleCredentials;
    String carpetaPadre = "1gnyYY2_1OmaEVq8-P_xxktm7weMII3Sp";

    private final EmailService emailService;
    @Autowired
    private IProveedorDocServices proveedorDocService;
    @Autowired
    private IFormularioProcesoServices formularioProcesoService;
    @Autowired
    private IPeriodoEvaluacionServices periodoEvaluacionService;
    @Autowired
    private IFormularioDetalleServices formularioDetalleService;
    @Autowired
    private IProveedorServices proveedorService;

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IProveedorEvaServices proveedorEvaService;

    @Autowired
    private IProveedorDocArchivoServices proveedorDocArchivoService;

    public ProveedorDocArchivoRestController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/proveedorDocArchivo")
    public ResponseEntity<?> opcionesPost(@RequestParam(name = "file", required = false) MultipartFile file,
                                          @RequestParam(name = "opcion", required = false) String opcion,
                                          @RequestParam(name = "nombreDocumento", required = false) String nombreDocumento,
                                          @RequestParam(name="prv_id", required = false) Integer prvId,
                                          @RequestParam(name = "fop_id", required = false) Integer fopId ,
                                          @RequestParam(name = "fod_id", required = false) Integer fodId ,
                                          @RequestParam(name = "idUnico", required = false) String idUnico ,
                                          @RequestParam(name = "id_emppal", required = false) Integer idEmppal ,
                                          @RequestParam(name = "per_id", required = false) Integer perId ) throws IOException, GeneralSecurityException {



        if (opcion != null) {
            switch (opcion) {
                case "descargar":
                    String idDocumento = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId).getPrdData();
                    byte[] archivo = proveedorDocArchivoService.descargarArchivo(idDocumento);
                    return ResponseEntity.ok(archivo);
                case "descargarArchivoId":
                    System.out.println(idUnico);
                    byte[] archivoPropio = proveedorDocArchivoService.descargarArchivo(idUnico);
                    return ResponseEntity.ok(archivoPropio);
                case "borrar":
                    ProveedorDoc miArchivo = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId);
                    String dato = miArchivo.getPrdData();
                    proveedorDocArchivoService.borrarArchivo(dato);
                    miArchivo.setPrdData("");
                    miArchivo.setPrdEstadoDocumental("P");
                    proveedorDocService.actualizarProveedorDoc(miArchivo);
                    return ResponseEntity.ok("borrado");
                case "guardarDocumentoPropio":
                    if (file == null || !file.getContentType().equals("application/pdf") || file.getSize() > 20971520) return null;
                    String archivoD = proveedorDocArchivoService.cargarArchivo("1AaorBQQI4JxyOeAhIti84O-GJYtx0Obt", nombreDocumento, file);
                    String dataArchivo = archivoD;
                    FormularioDetalle formularioDetallesDbI = formularioDetalleService.encontrarFormularioDetallesPorId(fodId,null);
                    formularioDetallesDbI.setFodAdjunto(dataArchivo);
                    formularioDetallesDbI.setFodTipo("file");
                    formularioDetalleService.actualizarFormularioDetalle(formularioDetallesDbI);
                    return ResponseEntity.ok("actualizado");
                case "guardarLogo":
                    if (file == null || !file.getContentType().equals("image/png") || file.getSize() > 20971520) return null;
                    Cliente miCliente = clienteService.encontrarClientePorId(idEmppal);
                    if (miCliente == null) return null;

                    String archivoLogo = proveedorDocArchivoService.cargarArchivo("1AaorBQQI4JxyOeAhIti84O-GJYtx0Obt", nombreDocumento, file);
                    String dataArchivoL = archivoLogo;
                    miCliente.setNitLogo(dataArchivoL);
                    clienteService.actualizarCliente(miCliente);

                    return ResponseEntity.ok("actualizado");
            }
        }

        if (file == null || !file.getContentType().equals("application/pdf") || file.getSize() > 20971520) return null;

        String carpetaProveedorGeneral = "";
        String carpetaPeriodoGeneral = "";
        String archivoDataGenerado = "";
        FormularioProceso miFormularioProceso = null;
        PeriodoEvaluacion miPeriodo = null;
        String nombreCarpetaPeriodo = "";
        String nombreArchivo = "";
        Proveedor miProveedor = null;
        String carpetaProveedor = "";
        String nombreCarpetaProveedor = "";

        if (perId != null) {
            miPeriodo = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perId, null);
            nombreCarpetaPeriodo = miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            nombreArchivo = "Evaluación del periodo " + miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId);
            carpetaProveedor = miProveedor.getPrvCarpeta();
            nombreCarpetaProveedor = miProveedor.getPrvNombre() + "-" + miProveedor.getTdcTd() + "-" + miProveedor.getPrvNd() + "-" + miProveedor.getIdEmppal();
        } else {
            miFormularioProceso = formularioProcesoService.encontrarFormulariosPorId(fopId);
            miPeriodo = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(miFormularioProceso.getPerId(), null);
            nombreCarpetaPeriodo = miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            nombreArchivo = formularioDetalleService.encontrarFormularioDetallesPorId(miFormularioProceso.getFodId(), null).getFodNombre();
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId);
            carpetaProveedor = miProveedor.getPrvCarpeta();
            nombreCarpetaProveedor = miProveedor.getPrvNombre() + "-" + miProveedor.getTdcTd() + "-" + miProveedor.getPrvNd() + "-" + miProveedor.getIdEmppal();
        }

        if (carpetaProveedor.equals("")) {
            String idCarpeta = proveedorDocArchivoService.crearCarpetaProveedor(carpetaPadre, nombreCarpetaProveedor);
            miProveedor.setPrvCarpeta(idCarpeta);
            proveedorService.actualizarProveedor(miProveedor);
            carpetaProveedorGeneral = miProveedor.getPrvCarpeta();

        } else {
            try {
                Boolean encontrado = proveedorDocArchivoService.verificarCarpetaProveedor(carpetaPadre, carpetaProveedor);
                if (!encontrado) {
                    String idCarpeta = proveedorDocArchivoService.crearCarpetaProveedor(carpetaPadre, nombreCarpetaProveedor);
                    miProveedor.setPrvCarpeta(idCarpeta);
                    proveedorService.actualizarProveedor(miProveedor);
                    carpetaProveedorGeneral = idCarpeta;
                } else {
                    carpetaProveedorGeneral = miProveedor.getPrvCarpeta();
                }
            } catch (IOException e) {
                System.out.println(e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
            }
        }
        FileList result = proveedorDocArchivoService.verificarCarpetaPeriodo(carpetaProveedorGeneral, nombreCarpetaPeriodo);
        if (result.getFiles().size() == 0) {
            String idCarpeta = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaProveedorGeneral, nombreCarpetaPeriodo);
            carpetaPeriodoGeneral = idCarpeta;

        } else {
            carpetaPeriodoGeneral = result.getFiles().get(0).getId();
        }

        FileList resultD = proveedorDocArchivoService.verificarDocumentoDoble(carpetaPeriodoGeneral, nombreArchivo);

        if (resultD.getFiles().size() == 0) {
            String archivo = proveedorDocArchivoService.cargarArchivo(carpetaPeriodoGeneral, nombreArchivo, file);
            archivoDataGenerado = archivo;
        } else {
            try {
                String archivoId = resultD.getFiles().get(0).getId();
                Boolean comparar = proveedorDocArchivoService.compararArchivos(file, archivoId);

                if (comparar) {
                    return new ResponseEntity<>("Archivos iguales", HttpStatus.OK);
                } else {
                    String archivo = proveedorDocArchivoService.cargarArchivo(carpetaPeriodoGeneral, nombreArchivo, file);
                    archivoDataGenerado = archivo;
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e, HttpStatus.OK);
            }
        }
        if (perId == null) {
            ProveedorDoc proveedorDocsDb = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId);
            if (proveedorDocsDb != null) {
                proveedorDocsDb.setPrdData(archivoDataGenerado);
                proveedorDocsDb.setPrdEstadoDocumental("A");
                proveedorDocService.actualizarProveedorDoc(proveedorDocsDb);
                return new ResponseEntity<>(proveedorDocsDb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        } else {
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId);
            emailService.sendListEmail(miProveedor.getPrvCorreo(), file , miPeriodo.getPerNombre(), miPeriodo.getPerFechaEvaluacion());
            ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(perId, prvId);
            proveedorEvasDb.setPreEstado("E");
            proveedorEvaService.actualizarProveedorEva(proveedorEvasDb);

            return  null;

        }

    }

}