package com.plprv.PlataformaProveedores.controller;

import com.google.api.services.drive.model.FileList;
import com.mysql.cj.xdevapi.Client;
import com.plprv.PlataformaProveedores.entity.*;
import com.plprv.PlataformaProveedores.service.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ProveedorDocArchivoRestController {

    private static final long TAMANOMAXIMO = 52428800;
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
    private IProcesoServices procesoServices;

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IProveedorEvaServices proveedorEvaService;

    @Autowired
    private IProveedorDocArchivoServices proveedorDocArchivoService;

    public ProveedorDocArchivoRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private ObtenerUsuarioAud obtenerUsuarioAud;
    @PostMapping("/proveedorDocArchivo")
    @PreAuthorize("hasAnyAuthority('superadministrador','administrador','lector','proveedor')")
    public ResponseEntity<?> opcionesPost(@RequestParam(name = "file", required = false) MultipartFile file,
                                          @RequestParam(name = "opcion", required = false) String opcion,
                                          @RequestParam(name = "nombreDocumento", required = false) String nombreDocumento,
                                          @RequestParam(name="prv_id", required = false) Integer prvId,
                                          @RequestParam(name = "fop_id", required = false) Integer fopId ,
                                          @RequestParam(name = "fod_id", required = false) Integer fodId ,
                                          @RequestParam(name = "idUnico", required = false) String idUnico ,
                                          @RequestParam(name = "per_id", required = false) Integer perId , HttpServletRequest request) throws IOException, GeneralSecurityException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Integer miIdEmppal = obtenerUsuarioAud.obtnerIdEmppalToken (token);


        if (opcion != null) {
            switch (opcion) {
                case "descargar" : {
                    String idDocumento = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId,miIdEmppal).getPrdData();
                    byte[] archivo = proveedorDocArchivoService.descargarArchivo(idDocumento);
                    return ResponseEntity.ok(archivo);
                }
                case "descargarArchivoId" : {
                    byte[] archivoPropio = proveedorDocArchivoService.descargarArchivo(idUnico);
                    return ResponseEntity.ok(archivoPropio);
                }
                case "borrar" : {
                    ProveedorDoc miArchivo = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId,miIdEmppal);
                    String dato = miArchivo.getPrdData();
                    proveedorDocArchivoService.borrarArchivo(dato);
                    miArchivo.setPrdData("");
                    miArchivo.setPrdEstadoDocumental("P");
                    proveedorDocService.actualizarProveedorDoc(miArchivo);
                    return ResponseEntity.ok("borrado");
                }
                case "guardarDocumentoPropio" : {
                    if (file == null || !Objects.equals(file.getContentType(), "application/pdf")) return null;
                    if (file.getSize() > TAMANOMAXIMO) return ResponseEntity.ok("archivo_pesado");
                    String dataArchivo = proveedorDocArchivoService.cargarArchivo("1N5vx0_Bhh1yGd2iztYtMD7Eu397BydOG", nombreDocumento, file);
                    FormularioDetalle formularioDetallesDbI = formularioDetalleService.encontrarFormularioDetallesPorId(fodId, null,miIdEmppal);
                    formularioDetallesDbI.setFodAdjunto(dataArchivo);
                    formularioDetallesDbI.setFodTipo("file");
                    formularioDetalleService.actualizarFormularioDetalle(formularioDetallesDbI);
                    return ResponseEntity.ok("actualizado");
                }
                case "guardarLogo" : {
                    if (file == null || !file.getContentType().startsWith("image/")) return null;
                    if (file.getSize() > TAMANOMAXIMO) return ResponseEntity.ok("archivo_pesado");
                    Cliente miCliente = clienteService.encontrarClientePorId(miIdEmppal);
                    if (miCliente == null) return null;
                    String dataArchivoL = proveedorDocArchivoService.cargarArchivo("1R6VMe48YeYOspJ6g1TI2HABw8medWHfv", nombreDocumento, file);
                    miCliente.setNitLogo(dataArchivoL);
                    clienteService.actualizarCliente(miCliente);
                    return ResponseEntity.ok("actualizado");
                }
            }
        }



        if (file == null || !Objects.equals(file.getContentType(), "application/pdf")) return null;
        if (file.getSize() > TAMANOMAXIMO ) return ResponseEntity.ok("archivo_pesado");

        String carpetaProceso = "";
        String nombreCarpetaProceso = "";
        String carpetaEmpresa = "";
        String nombreCarpetaEmpresa = "";
        String carpetaProveedorGeneral = "";
        String carpetaPeriodoGeneral = "";
        String archivoDataGenerado = "";
        FormularioProceso miFormularioProceso = null;
        PeriodoEvaluacion miPeriodo = null;
        Cliente miCliente = null;
        Proceso miProceso = null;
        String nombreCarpetaPeriodo = "";
        String nombreArchivo = "";
        Proveedor miProveedor = null;
        String carpetaProveedor = "";
        String nombreCarpetaProveedor = "";



        if (perId != null) {
            miPeriodo = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(perId, null,miIdEmppal);
            nombreCarpetaPeriodo = miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            nombreArchivo = "Evaluación del periodo " + miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId,miIdEmppal);
            if (miProveedor.getPrvCarpeta() != null) carpetaProveedor = miProveedor.getPrvCarpeta();
            nombreCarpetaProveedor = miProveedor.getPrvNombre() + "-" + miProveedor.getTdcTd() + "-" + miProveedor.getPrvNd() + "-" + miProveedor.getIdEmppal();
            miCliente = clienteService.encontrarClientePorId(miIdEmppal);
            carpetaEmpresa = miCliente.getNitCarpeta();
            nombreCarpetaEmpresa = miCliente.getNitNombre() + "-" + miCliente.getNitNd();
            miProceso = procesoServices.encontrarProcesosPorId(miProveedor.getProId(),"A",miIdEmppal);
            carpetaProceso = miProceso.getProCarpeta();
            nombreCarpetaProceso = miProceso.getProNombre();

        } else {
            miFormularioProceso = formularioProcesoService.encontrarFormulariosPorId(fopId,miIdEmppal);
            miPeriodo = periodoEvaluacionService.encontrarPeriodoEvaluacionsPorId(miFormularioProceso.getPerId(), null,miIdEmppal);
            nombreCarpetaPeriodo = miPeriodo.getPerNombre() + "-" + miPeriodo.getPerFechaEvaluacion();
            nombreArchivo = formularioDetalleService.encontrarFormularioDetallesPorId(miFormularioProceso.getFodId(), null,miIdEmppal).getFodNombre();
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId,miIdEmppal);
            carpetaProveedor = miProveedor.getPrvCarpeta();
            nombreCarpetaProveedor = miProveedor.getPrvNombre() + "-" + miProveedor.getTdcTd() + "-" + miProveedor.getPrvNd() + "-" + miProveedor.getIdEmppal();
            miCliente = clienteService.encontrarClientePorId(miIdEmppal);
            carpetaEmpresa = miCliente.getNitCarpeta();
            nombreCarpetaEmpresa = miCliente.getNitNombre() + "-" + miCliente.getNitNd();
            miProceso = procesoServices.encontrarProcesosPorId(miProveedor.getProId(),"A",miIdEmppal);
            carpetaProceso = miProceso.getProCarpeta();
            nombreCarpetaProceso = miProceso.getProNombre();
        }

        if (carpetaEmpresa != null){
            Boolean result = proveedorDocArchivoService.verificarCarpetaProveedor(carpetaPadre, carpetaEmpresa);
            if (!result) {
                carpetaEmpresa = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaPadre, nombreCarpetaEmpresa);
                miCliente.setNitCarpeta(carpetaEmpresa);
                clienteService.actualizarCliente(miCliente);
            }
        }else{
            carpetaEmpresa = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaPadre, nombreCarpetaEmpresa);
            miCliente.setNitCarpeta(carpetaEmpresa);
            clienteService.actualizarCliente(miCliente);
        }


        if (carpetaProceso != null){
            Boolean result = proveedorDocArchivoService.verificarCarpetaProveedor(carpetaEmpresa, carpetaProceso);
            if (!result) {
                carpetaProceso = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaEmpresa, nombreCarpetaProceso);
                miProceso.setProCarpeta(carpetaProceso);
                procesoServices.actualizarProceso(miProceso);
            }
        }else{
            carpetaProceso = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaEmpresa, nombreCarpetaProceso);
            miProceso.setProCarpeta(carpetaProceso);
            procesoServices.actualizarProceso(miProceso);
        }

        if (carpetaProveedor == null) {
            String idCarpeta = proveedorDocArchivoService.crearCarpetaProveedor(carpetaProceso, nombreCarpetaProveedor);
            miProveedor.setPrvCarpeta(idCarpeta);
            proveedorService.actualizarProveedor(miProveedor);
            carpetaProveedorGeneral = miProveedor.getPrvCarpeta();
        } else {
            try {
                boolean encontrado = proveedorDocArchivoService.verificarCarpetaProveedor(carpetaProceso, carpetaProveedor);
                if (!encontrado) {
                    String idCarpeta = proveedorDocArchivoService.crearCarpetaProveedor(carpetaProceso, nombreCarpetaProveedor);
                    miProveedor.setPrvCarpeta(idCarpeta);
                    proveedorService.actualizarProveedor(miProveedor);
                    carpetaProveedorGeneral = idCarpeta;

                } else {
                    carpetaProveedorGeneral = miProveedor.getPrvCarpeta();
                }
            } catch (IOException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
            }
        }

        FileList result = proveedorDocArchivoService.verificarCarpetaPeriodo(carpetaProveedorGeneral, nombreCarpetaPeriodo);
        if (result.getFiles().size() == 0) {
            carpetaPeriodoGeneral = proveedorDocArchivoService.crearCarpetaPeriodo(carpetaProveedorGeneral, nombreCarpetaPeriodo);

        } else {
            carpetaPeriodoGeneral = result.getFiles().get(0).getId();
        }

        FileList resultD = proveedorDocArchivoService.verificarDocumentoDoble(carpetaPeriodoGeneral, nombreArchivo);

        if (resultD.getFiles().size() == 0) {
            archivoDataGenerado = proveedorDocArchivoService.cargarArchivo(carpetaPeriodoGeneral, nombreArchivo, file);
        } else {
            try {
                String archivoId = resultD.getFiles().get(0).getId();
                Boolean comparar = proveedorDocArchivoService.compararArchivos(file, archivoId);

                if (comparar) {
                    return new ResponseEntity<>("Archivos iguales", HttpStatus.OK);
                } else {
                    archivoDataGenerado = proveedorDocArchivoService.cargarArchivo(carpetaPeriodoGeneral, nombreArchivo, file);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e, HttpStatus.OK);
            }
        }
        if (perId == null) {
            ProveedorDoc proveedorDocsDb = proveedorDocService.encontrarProveedorDocsPorFopId(fopId, prvId,miIdEmppal);
            if (proveedorDocsDb != null) {
                proveedorDocsDb.setPrdData(archivoDataGenerado);
                proveedorDocsDb.setPrdEstadoDocumental("A");
                proveedorDocService.actualizarProveedorDoc(proveedorDocsDb);
                return new ResponseEntity<>(proveedorDocsDb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        } else {
            miProveedor = proveedorService.encontrarProveedoresSoloPorId(prvId,miIdEmppal);
            emailService.sendListEmail(miProveedor.getPrvCorreo(), file , miPeriodo.getPerNombre(),
                    miPeriodo.getPerFechaEvaluacion(),carpetaPeriodoGeneral);
            ProveedorEva proveedorEvasDb = proveedorEvaService.encontrarProveedorEvaPorPerId(perId, prvId,miIdEmppal);
            proveedorEvasDb.setPreEstado("E");
            proveedorEvaService.actualizarProveedorEva(proveedorEvasDb);

            return  null;

        }

    }

}