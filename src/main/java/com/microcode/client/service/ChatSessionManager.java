package com.microcode.client.service;

import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Setter
@AllArgsConstructor
@EnableScheduling
public class ChatSessionManager {

    private final ConcurrentMap<String, Chat> activeChats = new ConcurrentHashMap<>();
    private final HttpServletRequest request;

    public void updateChatActivity(String chatId, ContentMessage message) {
        try {
            activeChats.putIfAbsent(chatId, new Chat(chatId));
        } catch (Exception ignored) {
        }
    }

    public Chat getChatById(String chatId) {
        return activeChats.get(chatId);
    }

    public boolean isAuthorized(String chatId){
        return activeChats.get(chatId).getChatAuthenticated();
    }

    public void setChatById(String chatId, Chat partialChat) {
        activeChats.computeIfPresent(chatId, (id, existingChat) -> {
            if (partialChat.getChatAuthenticated() != null)
                existingChat.setChatAuthenticated(partialChat.getChatAuthenticated());

            if (partialChat.getChatCode() != null)
                existingChat.setChatCode(partialChat.getChatCode());

            if (partialChat.getTypeDocument() != null)
                existingChat.setTypeDocument(partialChat.getTypeDocument());

            if (partialChat.getDocument() != null)
                existingChat.setDocument(partialChat.getDocument());

            if (partialChat.getChatMail() != null)
                existingChat.setChatMail(partialChat.getChatMail());

            if (partialChat.getChatDateAuthorized() != null)
                existingChat.setChatDateAuthorized(partialChat.getChatDateAuthorized());

            if (partialChat.getChatIp() != null)
                existingChat.setChatIp(partialChat.getChatIp());


            return existingChat;
        });
    }

    public boolean validateTime(Chat chat) {
        Date lastModified = chat.getChatDateAuthorized();
        if (lastModified == null) return true;
        Date now = new Date();
        long diffInMillis = now.getTime() - lastModified.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        boolean validate = diffInMinutes > 15;
        if(validate) activeChats.remove(chat.getChatId());
        return validate;
    }

    @Scheduled(fixedRate = 600000) // 600000ms = 10 minutos
    public void validateInactiveChats(){
        for(Chat chat : activeChats.values()) {
            this.validateTime(chat);
        }
    }



}
