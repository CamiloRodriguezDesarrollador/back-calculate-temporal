//package com.microcode.client.controller;
//
//import com.microcode.client.entity.general.Chat;
//import com.microcode.client.entity.mysql.Action;
//import com.microcode.client.service.manage.ManageAdditionalServices;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//
//@Slf4j
//@RequestMapping("/api/chat/test")
//@AllArgsConstructor
//@RestController
//public class TestController {
//
//    private final ManageAdditionalServices manageAdditionalServices;
//
//    @PostMapping("/aditional")
//    public Object generate() throws IOException {
//        String detail = "16-01-2026 - 31-01-2026";
//        Action action = new Action();
//        Chat chat = Chat.builder()
//                .tdcTd("NI")
//                .empNd(800165661L)
//                .ctoNumber(12393L)
//                .build();
//
//
//        Object result = manageAdditionalServices.generateCertPay(
//                detail,
//                action,
//                chat
//        );
//
//        System.out.println(result);
//          return result;
//
//    }
//
//
//
//
//}
