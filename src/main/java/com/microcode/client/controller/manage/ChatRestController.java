package com.microcode.client.controller.manage;

import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.entity.*;
import com.microcode.client.service.chat.ConsumeChatService;
import com.microcode.client.service.oracle.CertificatesService;
import com.microcode.client.service.oracle.OptionsManageService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/chats")
public class ChatRestController {

    private final ConsumeChatService consumeChatService;
    private final CertificatesService certificatesService;
    private ChatSessionManager chatSessionManager;

    @GetMapping("/active/chats")
    public ConcurrentMap<String, Chat> getActiveChats() {
        return chatSessionManager.getActiveChats();
    }

    @PostMapping("/sendMessageChatId")
    public String sendMessage(
                            @RequestParam String chatId,
                            @RequestParam String message,
                            @RequestParam String requestType,
                            @RequestParam String options) {
        ContentResponse contentResponse = new ContentResponse();
        contentResponse.setOptions(OptionsManageService.getOptionsByActionWithName(options));
        contentResponse.setActionMessage(message);
        contentResponse.setActionRequest(requestType);
        consumeChatService.sendMessageToChat(chatId, contentResponse);
        return "created";
    }


    @PostMapping("/jasper")
    public String getJasper() throws JRException, MalformedURLException, SQLException {
//        certificatesService.getDataCertificatePlanilla();
        return "";

//        String jrxmlPath = "src/main/resources/templates/ComprobanteDePago.jrxml";
//        String jasperPath = "src/main/resources/templates/ComprobanteDePago.jasper";
//        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
//        System.out.println("Reporte compilado con éxito.");

    }




}
