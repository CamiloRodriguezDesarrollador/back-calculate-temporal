package com.microcode.client.controller;

import com.microcode.client.entity.general.*;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.StatusChat;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.RegisterChatServices;
import com.microcode.client.service.mysql.StatusChatServices;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

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

        if (typeChat == null && message != null) {
            typeChat = switch (message.getActionId()) {
                case 1 -> 1;
                case 200 -> 2;
                default -> null;
            };
        }

        statusChatServices.create(
                StatusChat.builder()
                        .chatId(chatId)
                        .chatStatus("P")
                        .audDate(new Date())
                        .isHistory("S")
                        .build()
        );


        try{

            log.info("ChatId: {}" , chatId);
            log.info("TypeChat: {}" , typeChat);
            log.info("Message: {}" , message);

            List<Long> principalRequest = helperService.definePrincipalForCode(companyId);


            chatSessionManager.updateChatActivity(chatId, message);
            registerChatServices.createForMessage(chatId,message,"WP", companyId,typeChat);
            assert message != null;

            action =  actionServices.getActionForId(message.getActionId());
            log.info("Action {}" ,action);
            Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            message.getChatMessage().put("chatId", chatId);
            message.getChatMessage().put("principalRequest", principalRequest.toString());
            ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, message.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,"WP", companyId,typeChat);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);



            if(!responseWrap.getActionRequest().equals("error")){

                Chat chat = chatSessionManager.getChatById(chatId);

                boolean content = chat.getDocument() != null && chat.getTypeDocument() != null;

                List<Option> optionsYesOrNot = List.of(
                        new Option(content ? 2 : 1, "Si", "Y", null),
                        new Option(content ? 2 : 1, "No", "N", null)
                );

                List<Option> optionsNumber = List.of(
                        new Option(50, "Enviame el código de verificación", null, null)
                );

                if (responseWrap.getOptions() != null && !responseWrap.getOptions().isEmpty()) {
                    List<Option> optionsTemporal = responseWrap.getOptions();
                    for (int i = 0; i < optionsTemporal.size(); i++) {
                        Option opt = optionsTemporal.get(i);
                        String msg = opt.getActionMessage().replaceFirst("^\\d+\\.\\s*", "");
                        opt.setActionMessage((i + 1) + ". " + msg);
                    }
                    responseWrap.setOptions(optionsTemporal);
                }


                List<Option> options = switch (responseWrap.getActionRequest()) {
                    case "check"  -> optionsYesOrNot;
                    case "number" -> optionsNumber;
                    default       -> (responseWrap.getOptions() != null && !responseWrap.getOptions().isEmpty())
                            ? responseWrap.getOptions()
                            : Collections.emptyList();
                };



                statusChatServices.create(
                        StatusChat.builder()
                                .chatId(chatId)
                                .chatMessage(responseWrap.toString())
                                .chatOptions(options.isEmpty() ? null : options.toString())
                                .chatAction(typeChat == 1L ? 1 : 200)
                                .chatType(typeChat)
                                .audDate(new Date())
                                .chatStatus("C")
                                .isHistory("S")
                                .build()
                );
            }

            if(action.getActionId() == 1000 || action.getActionId() == 225){
                statusChatServices.delete(chatId);
                chatSessionManager.deleteChatId(chatId);
            }



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
            @RequestParam Integer companyId
    ){
        StatusChat currentStatus = statusChatServices.findChatById(chatId);
        if(currentStatus == null){

            List<Option> options = List.of(
                    new Option(1,   "1. 👷 Trabajador / Extrabajador",        null, null),
                    new Option(200, "2. 🏡 Cliente / Proveedor / Candidato",  null, null)
            );

            StatusChat status = StatusChat.builder()
                    .chatId(chatId)
                    .chatMessage(
                            "*¡Hola 👋!* Soy *Teo*, tu asistente virtual 🤖✨.\n" +
                                    "¡Estoy aquí para ayudarte! 😊\n\n" +
                                    "*Por favor elige una de las siguientes opciones:* 🙌\n\n" +
                                    "1. 👷 Trabajador / Extrabajador\n" +
                                    "2. 🏡 Cliente / Proveedor / Candidato"
                    )
                    .chatOptions(options.toString())
                    .audDate(new Date())
                    .isHistory("N")
                    .build();

            ContentResponse contentResponse = new ContentResponse();
            contentResponse.setActionMessage(status.getChatMessage());
            registerChatServices.createForResponse(chatId,contentResponse,"WP", companyId,null);


            statusChatServices.create(status);
            return status;

        }
        currentStatus.setIsHistory("S");
        return currentStatus;
    }

    @PostMapping("/inactive-session")
    public void inactive(
            @RequestBody ChatBody chatBody)
    {
        statusChatServices.delete(chatBody.getChatId());
        chatSessionManager.deleteChatId(chatBody.getChatId());
    }



}
