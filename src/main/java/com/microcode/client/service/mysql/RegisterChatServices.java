package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IRegisterChatDao;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.mysql.RegisterChat;
import com.microcode.client.service.ChatSessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterChatServices implements RegisterChatServicesI {

    private final IRegisterChatDao registerChatDao;
    private final ChatSessionManager chatSessionManager;
    private final HttpServletRequest request;

    @Override
    public void create(RegisterChat registerChat) {
        registerChatDao.save(registerChat);
    }

    @Override
    public void createForMessage(String chatId, ContentMessage contentMessage, String ip) {
        Chat chat = chatSessionManager.getChatById(chatId);

        try{
            RegisterChat reg = new RegisterChat();
            reg.setChatId(chatId);
            reg.setTypeDocument(chat.getTypeDocument());
            reg.setDocument(chat.getDocument());
            reg.setChatOwnerMessage(contentMessage.getChatOwnerMessage());
            reg.setChatMessage(contentMessage.toStringMessage());
            reg.setActionId(contentMessage.getActionId());
            reg.setChatIp(ip);
            create( reg );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createForResponse(String chatId, ContentResponse contentResponse, String ip) {
        Chat chat = chatSessionManager.getChatById(chatId);

        try {
            RegisterChat reg = new RegisterChat();
            reg.setChatId(chatId);
            reg.setTypeDocument(chat.getTypeDocument());
            reg.setDocument(chat.getDocument());
            reg.setChatOwnerMessage("bot");
            reg.setChatMessage(contentResponse.getActionMessage() +
                    (contentResponse.getOptions() != null ? " , Options: " + contentResponse.getOptions() : ""));
            reg.setActionId(contentResponse.getActionId());
            reg.setChatIp(ip);
            create( reg );
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

}
