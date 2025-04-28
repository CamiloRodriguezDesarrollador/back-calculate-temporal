package com.microcode.client.service;

import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.service.mysql.QuantityChatServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
    private final QuantityChatServices quantityChatServices;

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

            if (partialChat.getChatStart() != null)
                existingChat.setChatStart(partialChat.getChatStart());

            if (partialChat.getChatAttempts() != null)
                existingChat.setChatAttempts(partialChat.getChatAttempts());

            if (partialChat.getTdcTdFil() != null)
                existingChat.setTdcTdFil(partialChat.getTdcTdFil());

            if (partialChat.getEmpNdFil() != null)
                existingChat.setEmpNdFil(partialChat.getEmpNdFil());

            if (partialChat.getCtoNumber() != null)
                existingChat.setCtoNumber(partialChat.getCtoNumber());

            return existingChat;
        });
    }

    public boolean validateTime(Chat chat) {
        Date lastModified = chat.getChatDateAuthorized();
        if (lastModified == null && validateTimeStart(chat)) activeChats.remove(chat.getChatId());
        if (lastModified == null) return true;
        Date now = new Date();
        long diffInMillis = now.getTime() - lastModified.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        boolean validate = diffInMinutes > 5;
        if(validate) activeChats.remove(chat.getChatId());
        return validate;
    }

    public boolean validateTimeCode(Chat chat) {
        Date generateCode = chat.getChatDateCode();
        Date now = new Date();
        long diffInMillis = now.getTime() - generateCode.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        return diffInMinutes > 5;
    }

    public boolean validateTimeStart(Chat chat) {
        Date generateStart = chat.getChatStart() == null ? new Date() : chat.getChatStart();
        Date now = new Date();
        long diffInMillis = now.getTime() - generateStart.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        return diffInMinutes > 5;
    }

    public boolean validateAttemptsCode(Chat chat) {
        return chat.getChatAttempts() >= 3;
    }



    public QuantityResponse validityQuantityRequest(Action action, Chat chat, String detail ) {
        Integer quantity = action.getActionDaysQuantity();
        if(quantity == null) return new QuantityResponse(null,false);

        Date endDate = new Date();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocalDate = endLocalDate.minusDays(quantity);


        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<QuantityChat> quantityChats = quantityChatServices.findQuantityForDocumentAndAction(
                action.getActionId(), chat.getTypeDocument(), chat.getDocument(), startDate, endDate, detail
        );


        if(quantityChats == null || quantityChats.isEmpty())  return new QuantityResponse(null,false);

        LocalDate dateLast = quantityChats.get(0).getAudDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(action.getActionDaysQuantity());

        Boolean isOver = quantityChats.size() >= action.getActionQuantity();
        return new QuantityResponse(dateLast,isOver);
    }

    public QuantityResponse validityQuantityRequest(Chat chat, String detail ) {

        Date endDate = new Date();
        Date startDate = new Date();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<QuantityChat> quantityChats = quantityChatServices.findQuantityForDocument(
                chat.getTypeDocument(), chat.getDocument(), startDate, endDate, detail
        );

        LocalDate dateLast = quantityChats.get(0).getAudDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(0);

        Boolean isOver = quantityChats.size() >= 50;

        return new QuantityResponse(dateLast,isOver);
    }

    @Scheduled(fixedRate = 600000) // 600000ms = 10 minutos
    public void validateInactiveChats(){
        for(Chat chat : activeChats.values()) {
            this.validateTime(chat);
        }
    }



}
