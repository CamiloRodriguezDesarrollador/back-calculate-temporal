package com.microcode.client.service.chat;

import com.microcode.client.entity.ContentResponse;
import com.microcode.client.service.mysql.Salt;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsumeChatService {

    private final SimpMessagingTemplate messagingTemplate;
    public ConsumeChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessageToChat(String chatId, ContentResponse response) {
        try {
            ContentResponse contentResponseCopy = ContentResponse.cloneContentResponse(response);
            contentResponseCopy.setActionMessage(Salt.wrapMessage(response.getActionMessage()));
            String destination = "/api/chat/company/chat/" + chatId;
            messagingTemplate.convertAndSend(destination, contentResponseCopy);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}