package com.microcode.client.controller.manage;

import com.microcode.client.entity.oracle.CertificatePay;
import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.*;
import com.microcode.client.service.jasper.JasperService;
import com.microcode.client.service.oracle.CertificatesService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/chats")
public class ChatRestController {

     private ChatSessionManager chatSessionManager;

    @GetMapping("/active/chats")
    public ConcurrentMap<String, Chat> getActiveChats() {
        return chatSessionManager.getActiveChats();
    }

    @PostMapping("/jasper")
    public CertificatePay getJasper(@RequestParam Long eplNd ) throws JRException, MalformedURLException, SQLException {
//        String jrxmlPath = "src/main/resources/templates/ComprobanteDePago.jrxml";
//        String jasperPath = "src/main/resources/templates/ComprobanteDePago.jasper";
//        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
//        System.out.println("Reporte compilado con éxito.");

        String jrxmlPath3 = "src/main/resources/templates/ComprobanteDePago.jrxml";
        String jasperPath3 = "src/main/resources/templates/ComprobanteDePago.jasper";
        JasperCompileManager.compileReportToFile(jrxmlPath3, jasperPath3);

        String jrxmlPath2 = "src/main/resources/templates/ComprobanteDePagoSubRep1.jrxml";
        String jasperPath2 = "src/main/resources/templates/ComprobanteDePagoSubRep1.jasper";
        JasperCompileManager.compileReportToFile(jrxmlPath2, jasperPath2);
        System.out.println("Reporte compilado con éxito.");


//        System.out.println("Reporte compilado con éxito.");

        String jrxmlPath4 = "src/main/resources/templates/CertificacionLaboral.jrxml";
        String jasperPath4 = "src/main/resources/templates/CertificacionLaboral.jasper";
        JasperCompileManager.compileReportToFile(jrxmlPath4, jasperPath4);


//        certificatesService.getDataNumbersRads(1286413L,5L,"01/04/2025 - 15/04/2025");
//        jasperService.getCertificatePay(860090915L,"CC",1286413L,"01/04/2025 - 15/04/2025");
//       return certificatesService.getDataCertificatedPay(1L,1286413L,"16/04/2025 - 30/04/2025",478598L);
//        return certificatesService.getDataCertificatedDian(
//                "NI",860090915L,"CC",eplNd,"2024/01/01","2024/12/31"
//        );
        return null;
    }


}
