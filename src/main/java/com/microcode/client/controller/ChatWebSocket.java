package com.microcode.client.controller;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.chat.ChatSessionManagerI;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.*;
import com.microcode.client.service.manage.ManageServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.microcode.client.service.manage.ManageServices.*;

@Slf4j
@Controller
@AllArgsConstructor
@Component
public class ChatWebSocket {

    private final ActionServicesI actionServices;
    private ChatSessionManagerI chatSessionManager;
    private final RegisterChatServicesI registerChatServices;

    private final ManageServices manageServices;
    private final HelperService helperService;

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

            String clientIp = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("clientIp");

            if (message == null || message.getActionId() == null) return Salt.wrapContentResponde(manageServices.responseWithOptionsParam(error,action));
            ContentMessage messageUnwrapped = Salt.unwrapContentMessage(message);
            chatSessionManager.updateChatActivity(chatId, companyId.toString(), messageUnwrapped);
            registerChatServices.createForMessage(chatId,messageUnwrapped,clientIp, companyId,typeChat);
            action =  actionServices.getActionForId(messageUnwrapped.getActionId());
            Method methodAction = manageServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            messageUnwrapped.getChatMessage().put("chatId", chatId);
            messageUnwrapped.getChatMessage().put("principalRequest", principalRequest.toString());
            messageUnwrapped.getChatMessage().put("companyId", String.valueOf(companyId));
            ContentResponse resp = (ContentResponse) methodAction.invoke(manageServices, messageUnwrapped.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,clientIp, companyId,typeChat);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);
            if (responseWrap != null) responseWrap.setActionMessage(Salt.wrapMessage(resp.getActionMessage()));
            return responseWrap;

        }
        catch (Exception e){
            log.error("Error con el chatId {} : {}" , chatId, e.getMessage());
            ContentResponse responseWrap;
            Chat chat = chatSessionManager.getChatById(chatId,companyId.toString());
            if (chat == null) responseWrap = ContentResponse.cloneContentResponse(ManageServices.unauthorized);
            else if(chat.getChatAuthenticated() == null || !chat.getChatAuthenticated()) responseWrap = ContentResponse.cloneContentResponse(ManageServices.unauthorized);
            else responseWrap = ContentResponse.cloneContentResponse(manageServices.responseWithOptionsParam(notFound,action));
            return Salt.wrapContentResponde(responseWrap);
        }
    }





}
