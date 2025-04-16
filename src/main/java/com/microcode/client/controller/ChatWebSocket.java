package com.microcode.client.controller;

import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.*;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.mysql.RegisterChatServices;
import com.microcode.client.service.mysql.Salt;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import static com.microcode.client.service.oracle.ActionsOracleServices.error;
import static com.microcode.client.service.oracle.ActionsOracleServices.notFound;

@Controller
@AllArgsConstructor
public class ChatWebSocket {

    private ChatSessionManager chatSessionManager;
    private final RegisterChatServices registerChatServices;
    private final ActionsOracleServices actionsOracleServices;
    private final Salt salt;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/api/chat/company/chat/{chatId}")
    public ContentResponse greeting(@DestinationVariable String chatId,
                                    ContentMessage message,
                                    SimpMessageHeaderAccessor headerAccessor)
            {
        Action action = new Action();
        try{

//            Thread.sleep(5000);

            String clientIp = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("clientIp");

            if (message == null || message.getActionId() == null) return ActionsOracleServices.wrapMessage(ActionsOracleServices.responseWithOptionsParam(error,action));
            ContentMessage messageUnwrapped = Salt.unwrapContentMessage(message);

            chatSessionManager.updateChatActivity(chatId, messageUnwrapped);
            registerChatServices.createForMessage(chatId,messageUnwrapped,clientIp);

            action = actionsOracleServices.getActionForId(messageUnwrapped.getActionId());
            Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            messageUnwrapped.getChatMessage().put("chatId", chatId);
            ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, messageUnwrapped.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,clientIp);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);
            if (responseWrap != null) responseWrap.setActionMessage(Salt.wrapMessage(resp.getActionMessage()));
            return responseWrap;

        }
        catch (Exception e){
            ContentResponse responseWrap;
            Chat chat = chatSessionManager.getChatById(chatId);
            if(chat != null && !chat.getChatAuthenticated()) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.unauthorized);
            else if(chat != null) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.responseWithOptionsParam(notFound,action));
            else responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.responseWithOptionsParam(error,action));

            return ActionsOracleServices.wrapMessage(responseWrap);
        }

    }



}
