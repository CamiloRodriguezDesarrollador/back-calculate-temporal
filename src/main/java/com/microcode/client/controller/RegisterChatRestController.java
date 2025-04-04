package com.microcode.client.controller;

import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.*;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.QuantityChatServices;
import com.microcode.client.service.mysql.RegisterChatServices;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class RegisterChatRestController {

    private ChatSessionManager chatSessionManager;
    private final RegisterChatServices registerChatServices;
    private final ActionServices actionServices;
    private final ActionsOracleServices actionsOracleServices;
    private final QuantityChatServices quantityChatServices;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/company/chat/{chatId}")
    public ContentResponse greeting(@DestinationVariable String chatId,
                                    ContentMessage message,
                                    SimpMessageHeaderAccessor headerAccessor)
            {

        try{
            String clientIp = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("clientIp");

            if (message == null || message.getActionId() == null) return actionsOracleServices.notAction();

            chatSessionManager.updateChatActivity(chatId, message);
            registerChatServices.createForMessage(chatId,message,clientIp);

            Action action = actionServices.findActionById( message.getActionId());
            Chat chat = chatSessionManager.getChatById(chatId);

            List<QuantityChat> quantityChats = quantityChatServices.findQuantityForDocumentAndAction(
                    action.getActionId(), chat.getTypeDocument(), chat.getDocument()
            );

            if(quantityChats.size() >= action.getActionQuantity()){
                return actionsOracleServices.quantityMax();
            }

            Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionName(), Map.class);
            message.getChatMessage().put("chatId", chatId);
            message.getChatMessage().put("ip",clientIp);
            message.getChatMessage().put("actionId", action.getActionId().toString());
            ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, message.getChatMessage());
            registerChatServices.createForResponse(chatId,resp,clientIp);

            return resp;

        }catch (Exception e){
            Chat chat = chatSessionManager.getChatById(chatId);
            System.out.println(e.getMessage());
            if(chat.getChatAuthenticated()) return actionsOracleServices.notFound();
            return actionsOracleServices.unauthorized();


        }

    }



}
