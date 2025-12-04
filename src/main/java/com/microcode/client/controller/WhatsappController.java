package com.microcode.client.controller;

import com.microcode.client.entity.general.*;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.StatusChat;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.RegisterChatServices;
import com.microcode.client.service.mysql.Salt;
import com.microcode.client.service.mysql.StatusChatServices;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.microcode.client.service.oracle.ActionsOracleServices.error;
import static com.microcode.client.service.oracle.ActionsOracleServices.notFound;

@Slf4j
@RequestMapping("/api/chat/wp")
@AllArgsConstructor
@RestController
public class WhatsappController {

    private final HelperService helperService;
    private final ActionServices actionServices;
    private final StatusChatServices statusChatServices;
    private ChatSessionManager chatSessionManager;
    private final RegisterChatServices registerChatServices;
    private final ActionsOracleServices actionsOracleServices;

    @PostMapping("/send-message")
    public ContentResponse greeting(
            @RequestBody ChatBody chatBody)
        {
        Action action = new Action();
        String chatId = chatBody.getChatId();
        Integer companyId = chatBody.getCompanyId();
        Integer typeChat = chatBody.getTypeChat();
        ContentMessage message = chatBody.getMessage();
        try{

            log.info("ChatId: {}" , chatId);
            log.info("TypeChat: {}" , typeChat.toString());
            log.info("Message: {}" , message);

            List<Long> principalRequest = helperService.definePrincipalForCode(companyId);

            if (message == null || message.getActionId() == null) {

                ContentResponse response = actionsOracleServices.responseWithOptionsParam(error,action);

                statusChatServices.create(
                    StatusChat.builder()
                        .chatId(chatId)
                        .chatMessage("Intenta mas tarde.")
                        .chatOptions(null)
                        .audDate(new Date())
                        .chatAction(typeChat == 1L ? 1 : 200)
                        .isHistory("S")
                        .chatType(typeChat)
                        .build()
                );
                return response;
            }

            chatSessionManager.updateChatActivity(chatId, message);
            registerChatServices.createForMessage(chatId,message,"WP", companyId,typeChat);
            action =  actionServices.getActionForId(message.getActionId());
            log.info("Action {}" ,action);
            Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            message.getChatMessage().put("chatId", chatId);
            message.getChatMessage().put("principalRequest", principalRequest.toString());
            ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, message.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,"WP", companyId,typeChat);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);

            List<Option> optionsYesOrNot = List.of(
                    new Option(2,   "Si",        "Y" ,null),
                    new Option(2, "No",  "N", null)
            );

            List<Option> optionsNumber = List.of(
                    new Option(50,   "Enviame el codio de verificación",        null ,null)
            );

            List<Option> options;

            if ("check".equals(responseWrap.getActionRequest())) {
                options = optionsYesOrNot;
            }
            else if ("number".equals(responseWrap.getActionRequest())) {
                options = optionsNumber;
            } else {
                options = (responseWrap.getOptions() != null && !responseWrap.getOptions().isEmpty())
                        ? responseWrap.getOptions()
                        : Collections.emptyList();
            }

            statusChatServices.create(
                    StatusChat.builder()
                            .chatId(chatId)
                            .chatMessage(responseWrap.toString())
                            .chatOptions(options.isEmpty() ? null : options.toString())
                            .chatAction(typeChat == 1L ? 1 : 200)
                            .audDate(new Date())
                            .isHistory("S")
                            .build()
            );

            return responseWrap;

        }
        catch (Exception e){
            log.error("Error con el chatId {} : {}" , chatId, e.getMessage());
            ContentResponse responseWrap;
            Chat chat = chatSessionManager.getChatById(chatId);
            if (chat == null) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.unauthorized);
            else if(chat.getChatAuthenticated() == null || !chat.getChatAuthenticated()) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.unauthorized);
            else responseWrap = ContentResponse.cloneContentResponse(actionsOracleServices.responseWithOptionsParam(notFound,action));
            return responseWrap;
        }
    }

    @GetMapping("/status")
    public StatusChat getStatusChat(
            @RequestParam String chatId,
            @RequestParam Long empNd
    ){
        StatusChat currentStatus = statusChatServices.findChatById(chatId);
        if(currentStatus == null){
            List<Option> options = List.of(
                    new Option(1,   "👷 Trabajador / Extrabajador",        null, null),
                    new Option(200, "🏡 Cliente / Proveedor / Candidato",  null, null)
            );

            StatusChat status = StatusChat.builder()
                    .chatId(chatId)
                    .chatMessage("¡Hola 👋! Soy Teo tu asistente virtual. ¡Estoy aquí para ayudarte! 😊" +
                            "Elige si eres trabajador, extrabajador, cliente, o candidato para una ayuda personalizada.")
                    .chatOptions(options.toString())
                    .audDate(new Date())
                    .isHistory("N")
                    .build();

//            chatSessionManager.setChatById(
//                    chatId,
//                    Chat.builder()
//                            .chatId(chatId)
//                            .chatStart(new Date())
//                            .empNd(empNd)
//                            .build()
//            );

            statusChatServices.create(status);
            return status;

        }
        currentStatus.setIsHistory("S");
        return currentStatus;
    }





}
