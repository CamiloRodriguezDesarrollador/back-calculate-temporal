package com.microcode.client.controller;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.RegisterChatServices;
import com.microcode.client.service.mysql.Salt;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.microcode.client.service.oracle.ActionsOracleServices.*;

@Controller
@AllArgsConstructor
@Component
public class ChatWebSocket {

    private final HelperService helperService;
    private final ActionServices actionServices;
    private ChatSessionManager chatSessionManager;
    private final RegisterChatServices registerChatServices;
    private final ActionsOracleServices actionsOracleServices;

    @MessageMapping("/chat/{chatId}/{companyId}/{typeChat}")
    @SendTo("/api/chat/company/chat/{chatId}")
    public ContentResponse greeting(@DestinationVariable String chatId,
                                    @DestinationVariable Integer companyId,
                                    @DestinationVariable Integer typeChat,
                                    ContentMessage message,
                                    SimpMessageHeaderAccessor headerAccessor)
            {
        Action action = new Action();
        try{
            List<Long> principalRequest = helperService.definePrincipalForCode(companyId);

//            Thread.sleep(5000);
            String clientIp = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("clientIp");

            if (message == null || message.getActionId() == null) return Salt.wrapContentResponde(actionsOracleServices.responseWithOptionsParam(error,action));
            ContentMessage messageUnwrapped = Salt.unwrapContentMessage(message);
            chatSessionManager.updateChatActivity(chatId, messageUnwrapped);
            registerChatServices.createForMessage(chatId,messageUnwrapped,clientIp, companyId,typeChat);
            action =  actionServices.getActionForId(messageUnwrapped.getActionId());
            Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            messageUnwrapped.getChatMessage().put("chatId", chatId);
            messageUnwrapped.getChatMessage().put("principalRequest", principalRequest.toString());
            messageUnwrapped.getChatMessage().put("typeChat", typeChat.toString());
            ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, messageUnwrapped.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,clientIp, companyId,typeChat);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);
            if (responseWrap != null) responseWrap.setActionMessage(Salt.wrapMessage(resp.getActionMessage()));

            return responseWrap;

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            ContentResponse responseWrap;
            Chat chat = chatSessionManager.getChatById(chatId);
            if (chat == null) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.unauthorized);
            else if(chat.getChatAuthenticated() == null || !chat.getChatAuthenticated()) responseWrap = ContentResponse.cloneContentResponse(ActionsOracleServices.unauthorized);
            else responseWrap = ContentResponse.cloneContentResponse(actionsOracleServices.responseWithOptionsParam(notFound,action));
            return Salt.wrapContentResponde(responseWrap);
        }

    }





}
