package com.microcode.client.controller.manage;

import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.*;
import com.microcode.client.service.jasper.JasperService;
import com.microcode.client.service.oracle.CertificatesService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/chats")
public class ChatRestController {

    private final CertificatesService certificatesService;
    private ChatSessionManager chatSessionManager;

    @GetMapping("/active/chats")
    public ConcurrentMap<String, Chat> getActiveChats() {
        return chatSessionManager.getActiveChats();
    }

    @PostMapping("/jasper")
    public void getJasper() throws JRException, MalformedURLException, SQLException {
        certificatesService.getDataCertificatedPay(1L,921599L,"01/01/2025 - 01/04/2025",478672L);
    }

}
