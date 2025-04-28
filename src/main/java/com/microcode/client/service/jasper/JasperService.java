package com.microcode.client.service.jasper;

import com.microcode.client.entity.oracle.CertificateJob;
import com.microcode.client.entity.oracle.CertificatePay;
import com.microcode.client.entity.oracle.TypeNovCompany;
import com.microcode.client.service.oracle.CertificatesService;
import com.microcode.client.service.oracle.TypeNovServices;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;

@Service
@AllArgsConstructor
public class JasperService {

    private final CertificatesService certificatesService;
    private final TypeNovServices typeNovServices;


    public byte[] exportToPdf(Map<String, Object> params,JRBeanCollectionDataSource ds, String route) throws JRException, FileNotFoundException {
        return JasperExportManager.exportReportToPdf(getReport(params,ds,route));
    }

    private JasperPrint getReport(Map<String, Object> params , JRBeanCollectionDataSource ds, String route) throws FileNotFoundException, JRException {
        return JasperFillManager.fillReport(JasperCompileManager.compileReport(
                ResourceUtils.getFile(route)
                        .getAbsolutePath()), params, new JREmptyDataSource());

    }

    public byte[] getCertificateJob(Long empNd, String tdcTd, Long ctoNumber) {
        TypeNovCompany typ = typeNovServices.findByIds(empNd,tdcTd,10L);
        CertificateJob cert = certificatesService.getDataCertificatedJob(typ.getTneCodigo(),ctoNumber,"N","QUIEN INTERESE");

        System.out.println(typ.toString());
        System.out.println(cert.toString());
        
        try{

            Map<String, Object> hm = new HashMap<>();
            hm.put("P_NOM_EMPRESA_PPAL", cert.getNombreEmpresaPrincipal());
            hm.put("P_ND_EMPRESA_PPAL", cert.getEmpresaNd());
            hm.put("P_RESPONSABLE_X_CARGO", cert.getRolResponsable());
            hm.put("P_TEXTO", cert.getTexto());
            hm.put("P_RESPONSABLE", cert.getResponsable());
            hm.put("P_AREA_RESPONSABLE", cert.getAreaResponsable());
            hm.put("P_NOM_EMPRESA_USUARIA", cert.getNombreEmpresaFilial());
            hm.put("P_LOGO", "attachment/" +  empNd + "/logo.jpg");
            hm.put("P_FIRMA","attachment/"  + empNd  + "/" + cert.getDirFirma());
            hm.put("P_NO_CONTRATO", ctoNumber);
            hm.put("P_CONFIRMACION", cert.getTextoConfirmacion());
            hm.put("P_INFO_EMPRESA", cert.getPieInfEmpresa());
            hm.put("P_TEXTO_LM", cert.getTextoLm());
            hm.put("P_TEXTO_NOTA", cert.getTextoNota());
            hm.put("P_TEXTO_TITULO", cert.getTextoTitulo());
            hm.put("P_TEXTO_ATTE", cert.getTextoAtte());

            return exportToPdf(hm,new JRBeanCollectionDataSource(null),"classpath:templates/CertificacionLaboral.jrxml");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public byte[] getCertificatePay(Long ctoNumber, String period) {

        try{
        CertificatePay cert = certificatesService.getDataCertificatedPay(
                11L,ctoNumber,period,1L);


            Map<String, Object> hm = new HashMap<>();
            hm.put("P_NOM_EMPRESA_PPAL", cert.getNombreEmpresa());
            hm.put("P_NOM_EMPRESA_FIL", cert.getNombreEmpresaFilial());
            hm.put("P_PERIODO_NOMINA", cert.getPeriodoNomina());
            hm.put("P_CONSECUTIVO_HPR", cert.getConsecutivoHpr());
            hm.put("P_NOMBRE_EMPLEADO", cert.getNombreEmpleado());
            hm.put("P_ID_EMPLEADO", cert.getIdentificacionEmpleado());
            hm.put("P_SUELDO", cert.getSueldo());
            hm.put("P_IPP", cert.getIpp());
            hm.put("P_RETEFUENTE", cert.getRtefte());
            hm.put("P_CENTRO_COSTO", cert.getCentroCosto());
            hm.put("P_CIUDAD", cert.getNombreCiudad());
            hm.put("P_MENSAJE_BASICO",cert.getMensajeBasico());
            hm.put("P_CESANTIAS", cert.getCesantias());
            hm.put("P_MENSAJE", cert.getMensaje());
            hm.put("P_OTRO_MENSAJE", cert.getOtroMensaje());
            hm.put("P_MONTO", cert.getMonto());
            hm.put("P_PRESTAMOS", cert.getTablaAhorroPrestamo());
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(cert.getTablaPago());
            hm.put("P_NO_CONTRATO", ctoNumber);

            return exportToPdf(hm,ds,"classpath:templates/ComprobanteDePago.jrxml");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public byte[] protectPdfWithPassword(byte[] pdfBytes, String password) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            document.protect(spp);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        }
    }
}
