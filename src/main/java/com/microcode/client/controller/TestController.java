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
//
//    @PostMapping("/aditional")
//    public Object generate() throws IOException {
////        String detail = "16-01-2026 - 31-01-2026";
//        String detail = "60000";
//        Action action = new Action();
//        action.setActionId(2013);
//        action.setActionOptionError("fedac-detail");
//
//        Chat chat = Chat.builder()
//                .tdcTd("NI")
//                .empNd(860090915L)
//                .ctoNumber(789882L)
//                .typeDocument("CC")
//                .document("1075539286")
//                .contractActive(true)
//                .chatId("3243242")
//                .perSigla("Q")
//                .build();
//
//        Object result = manageAdditionalServices.getDataFedacApproved(
//                detail,
//                action,
//                chat
//        );
//
//        System.out.println(result);
//        return result;
//
//    }
//
//
//
//
//}
