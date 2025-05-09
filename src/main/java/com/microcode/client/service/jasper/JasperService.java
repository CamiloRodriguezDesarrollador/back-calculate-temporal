package com.microcode.client.service.jasper;

import com.microcode.client.entity.oracle.CertificateJob;
import com.microcode.client.entity.oracle.CertificatePay;
import com.microcode.client.entity.oracle.TypeNovCompany;
import com.microcode.client.service.oracle.CertificatesService;
import com.microcode.client.service.oracle.TypeNovServices;
import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperService {

    private final CertificatesService certificatesService;
    private final TypeNovServices typeNovServices;
    private JasperReport certJobReportInitial;
    private JasperReport certPayReportInitial;


    @PostConstruct
    public void init() throws IOException, JRException {
        try{
            InputStream fileJob = new ClassPathResource("templates/CertificacionLaboral.jasper").getInputStream();
            certJobReportInitial = (JasperReport) JRLoader.loadObject(fileJob);

            InputStream filePay = new ClassPathResource("templates/ComprobanteDePago.jasper").getInputStream();
            certPayReportInitial = (JasperReport) JRLoader.loadObject(filePay);

        } catch (IOException e) {
            System.out.println("Error aca: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public JasperService(CertificatesService certificatesService, TypeNovServices typeNovServices) {
        this.certificatesService = certificatesService;
        this.typeNovServices = typeNovServices;
    }


    public byte[] getCertificateJob(Long empNd, String tdcTd, Long ctoNumber) {
        try {

            JasperReport cachedReportJob = certJobReportInitial;

            TypeNovCompany typ = typeNovServices.findByIds(empNd, tdcTd, 10L);
            CertificateJob cert = certificatesService.getDataCertificatedJob(typ.getTneCodigo(), ctoNumber, "N", "QUIEN INTERESE");
            Map<String, Object> hm = new HashMap<>();
            hm.put("P_NOM_EMPRESA_PPAL", cert.getNombreEmpresaPrincipal());
            hm.put("P_ND_EMPRESA_PPAL", cert.getEmpresaNd());
            hm.put("P_RESPONSABLE_X_CARGO", cert.getRolResponsable());
            hm.put("P_TEXTO", cert.getTexto());
            hm.put("P_RESPONSABLE", cert.getResponsable());
            hm.put("P_AREA_RESPONSABLE", cert.getAreaResponsable());
            hm.put("P_NOM_EMPRESA_USUARIA", cert.getNombreEmpresaFilial());
            hm.put("P_LOGO", "attachment/" + empNd + "/logo.jpg");
            hm.put("P_FIRMA", "attachment/" + empNd + "/" + cert.getDirFirma());
            hm.put("P_NO_CONTRATO", ctoNumber);
            hm.put("P_CONFIRMACION", cert.getTextoConfirmacion());
            hm.put("P_INFO_EMPRESA", cert.getPieInfEmpresa());
            hm.put("P_TEXTO_LM", cert.getTextoLm());
            hm.put("P_TEXTO_NOTA", cert.getTextoNota());
            hm.put("P_TEXTO_TITULO", cert.getTextoTitulo());
            hm.put("P_TEXTO_ATTE", cert.getTextoAtte());


            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    cachedReportJob,
                    hm,
                    new JREmptyDataSource()
            );

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public byte[] getCertificatePay(Long empNd, String tdcTd, Long ctoNumber, String period) {
        try{
            JasperReport cachedReportPay = certPayReportInitial;

            TypeNovCompany typ = typeNovServices.findByIds(empNd, tdcTd, 11L);

            List<Long> rads =certificatesService.getDataNumbersRads(
                    ctoNumber,typ.getTneCodigo(),period
            );

            List<JasperPrint> jpF = new ArrayList<>();
            if(rads == null || rads.isEmpty()) return null;

            for(Long rad : rads){
                CertificatePay cert = certificatesService.getDataCertificatedPay(
                        typ.getTneCodigo(),ctoNumber,period,rad
                );

                if(cert == null) return null;

                Map<String, Object> hm = new HashMap<>();
                hm.put("P_NOM_EMPRESA_PPAL",  cert.getNombreEmpresa() != null ? cert.getNombreEmpresa() : "");
                hm.put("P_NOM_EMPRESA_FIL", cert.getNombreEmpresaFilial() != null ? cert.getNombreEmpresaFilial() : "");
                hm.put("P_PERIODO_NOMINA", cert.getPeriodoNomina() != null ? cert.getPeriodoNomina() : "");
                hm.put("P_CONSECUTIVO_HPR", cert.getConsecutivoHpr() != null ? cert.getConsecutivoHpr() : "");
                hm.put("P_NOMBRE_EMPLEADO", cert.getNombreEmpleado() != null ? cert.getNombreEmpleado() : "");
                hm.put("P_ID_EMPLEADO", cert.getIdentificacionEmpleado() != null ? cert.getIdentificacionEmpleado() : "");
                hm.put("P_SUELDO", cert.getSueldo() != null ? cert.getSueldo() : "");
                hm.put("P_IPP", cert.getIpp() != null ? cert.getIpp() : "");
                hm.put("P_RETEFUENTE", cert.getRtefte() != null ? cert.getRtefte() : "");
                hm.put("P_CENTRO_COSTO", cert.getCentroCosto() != null ? cert.getCentroCosto() : "");
                hm.put("P_CIUDAD", cert.getNombreCiudad() != null ? cert.getNombreCiudad() : "");
                hm.put("P_MENSAJE_BASICO", cert.getMensajeBasico() != null ? cert.getMensajeBasico() : "");
                hm.put("P_CESANTIAS", cert.getCesantias() != null ? cert.getCesantias() : "");
                hm.put("P_MENSAJE", cert.getMensaje() != null ? cert.getMensaje() : "");
                hm.put("P_OTRO_MENSAJE", cert.getOtroMensaje() != null ? cert.getOtroMensaje() : "");
                hm.put("P_MONTO", cert.getMonto() != null ? cert.getMonto() : "");
                hm.put("P_MENSAJE2", cert.getMensaje2() != null ? cert.getMensaje2() : "");
                hm.put("P_PRESTAMOS", cert.getTablaAhorroPrestamo() != null ? cert.getTablaAhorroPrestamo() : "");
                hm.put("P_NO_CONTRATO", ctoNumber != null ? ctoNumber : "");

                hm.put("P_RUTA_REPORTES", "templates/");
                hm.put("P_RUTA_SEPARADOR_REP","");


                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(cert.getTablaPago());

                try{
                    JasperPrint jasperPrint = JasperFillManager.fillReport(cachedReportPay, hm, ds);
                    jpF.add(jasperPrint);
                }catch (Throwable e){
                    System.out.println(e.getMessage());
                }

            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jpF.get(0), byteArrayOutputStream);

            for (int i = 1; i < jpF.size(); i++) {
                JasperExportManager.exportReportToPdfStream(jpF.get(i), byteArrayOutputStream);
            }

            return byteArrayOutputStream.toByteArray();

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
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}

//        String jrxmlPath = "src/main/resources/templates/CertificacionLaboral.jrxml";
//        String jasperPath = "src/main/resources/templates/CertificacionLaboral.jasper";
//        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
//        System.out.println("Reporte compilado con éxito.");