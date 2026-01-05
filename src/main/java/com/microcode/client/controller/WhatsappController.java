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
    public ContentResponse greeting(@RequestBody ChatBody chatBody){
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

        statusChatServices.createPend(chatId);

        try{

            log.info("ChatId: {}, TypeChat: {}, Message: {}" , chatId, typeChat, message);
            List<Long> principalRequest = helperService.definePrincipalForCode(companyId);

            chatSessionManager.updateChatActivity(chatId, message);
            registerChatServices.createForMessage(chatId,message,"WP", companyId,typeChat);
            if (message == null) throw new IllegalArgumentException("El mensaje no puede ser null");

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
                StatusChat status = StatusChat.defineStatusStarted(chatId, chat, responseWrap, typeChat );
                statusChatServices.create(status);
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
            StatusChat status = StatusChat.defineStatusInitial(chatId,companyId );
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
