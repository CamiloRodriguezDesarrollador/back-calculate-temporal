package com.microcode.client.controller;

import com.microcode.client.clients.ConnectExternalServices;
import com.microcode.client.entity.general.*;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.StatusChat;
import com.microcode.client.service.chat.ChatSessionManagerI;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.*;
import com.microcode.client.service.manage.ManageServices;
import com.microcode.client.service.oracle.CertificatesServiceI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

import static com.microcode.client.service.manage.ManageServices.notFound;

@Slf4j
@RequestMapping("/api/chat/wp")
@AllArgsConstructor
@RestController
public class WhatsappController {

    private final HelperService helperService;
    private final ActionServicesI actionServices;
    private final StatusChatServicesI statusChatServices;
    private final CertificatesServiceI certificatesService;
    private final ConnectExternalServices connectExternalServices;
    private ChatSessionManagerI chatSessionManager;
    private final RegisterChatServicesI registerChatServices;
    private final ManageServices manageServices;

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

        statusChatServices.createPend(chatId, companyId);

        try{

            log.info("ChatId: {}, TypeChat: {}, Message: {}" , chatId, typeChat, message);
            List<Long> principalRequest = helperService.definePrincipalForCode(companyId);

            chatSessionManager.updateChatActivity(chatId, companyId.toString(), message);
            registerChatServices.createForMessage(chatId,message,"WP", companyId,typeChat);
            if (message == null) throw new IllegalArgumentException("El mensaje no puede ser null");

            action =  actionServices.getActionForId(message.getActionId());
            log.info("Action {}" ,action);
            Method methodAction = manageServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
            message.getChatMessage().put("chatId", chatId);
            message.getChatMessage().put("principalRequest", principalRequest.toString());
            message.getChatMessage().put("companyId", companyId.toString());
            ContentResponse resp = (ContentResponse) methodAction.invoke(manageServices, message.getChatMessage(), action);
            registerChatServices.createForResponse(chatId,resp,"WP", companyId,typeChat);
            ContentResponse responseWrap = ContentResponse.cloneContentResponse(resp);

            if(!responseWrap.getActionRequest().equals("error")){
                Chat chat = chatSessionManager.getChatById(chatId,companyId.toString());
                StatusChat status = StatusChat.defineStatusStarted(chatId, chat, responseWrap, typeChat, companyId );
                statusChatServices.create(status);
            }

            if(action.getActionId() == 1000 || action.getActionId() == 225 || responseWrap.getActionMessage().contains("Para utilizar esta opción debes estar autenticado")){
                statusChatServices.delete(chatId, companyId);
                chatSessionManager.deleteChatId(chatId, companyId);
            }

            return responseWrap;

        }
        catch (Exception e){
            log.error("Error con el chatId {} : {}" , chatId, e.getMessage());
            ContentResponse responseWrap;
            Chat chat = chatSessionManager.getChatById(chatId,companyId.toString());
            if (chat == null) responseWrap = ContentResponse.cloneContentResponse(ManageServices.unauthorized);
            else if(chat.getChatAuthenticated() == null || !chat.getChatAuthenticated()) responseWrap = ContentResponse.cloneContentResponse(ManageServices.unauthorized);
            else responseWrap = ContentResponse.cloneContentResponse(manageServices.responseWithOptionsParam(notFound,action));
            return responseWrap;
        }
    }

    @GetMapping("/status")
    public StatusChat getStatusChat(
            @RequestParam String chatId,
            @RequestParam Integer companyId
    ){
        StatusChat currentStatus = statusChatServices.findChatById(chatId,companyId);
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
        statusChatServices.delete(chatBody.getChatId(), chatBody.getCompanyId());
        chatSessionManager.deleteChatId(chatBody.getChatId(), chatBody.getCompanyId());
    }

    @PostMapping("/delete-session")
    public void inactive()
    {
        statusChatServices.deleteAll();
        chatSessionManager.deleteChats();
    }

//    @PostMapping("/carnet-arl")
//    public void generate()
//    {
//        String urlCarnet = certificatesService.getDataCarnetArl(
//                "NI",
//                860090915L,
//                1286413L
//        );
//
//        System.out.println(urlCarnet);
//        if (urlCarnet == null) return;
//
//        byte[] carnet = connectExternalServices.connectUrl(urlCarnet);
//        System.out.println(carnet.length);
//
//    }




}
