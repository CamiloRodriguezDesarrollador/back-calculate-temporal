package com.microcode.client.service.chat;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.service.mysql.QuantityChatServices;
import com.microcode.client.service.mysql.StatusChatServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ChatSessionManager.class);
    private final ConcurrentMap<String, Chat> activeChats = new ConcurrentHashMap<>();
    private final HttpServletRequest request;
    private final QuantityChatServices quantityChatServices;
    private final StatusChatServices statusChatServices;

    public void updateChatActivity(String chatId, ContentMessage message) {
        try {
            activeChats.putIfAbsent(chatId, new Chat(chatId));
        } catch (Exception ignored) {
        }
    }

    public Chat getChatById(String chatId) {
        return activeChats.get(chatId);
    }

    public Chat getAuthorizedChatByDocumentAndType(String document, String typeDocument) {
        return activeChats.values().stream()
                .filter(chat -> document.equals(chat.getDocument()))
                .filter(chat -> typeDocument.equals(chat.getTypeDocument()))
                .filter(Chat::getChatAuthenticated)
                .findFirst()
                .orElse(null);
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

            if (partialChat.getTdcTd() != null)
                existingChat.setTdcTd(partialChat.getTdcTd());

            if (partialChat.getEmpNd() != null)
                existingChat.setEmpNd(partialChat.getEmpNd());

            if (partialChat.getPrincipalRequest() != null)
                existingChat.setPrincipalRequest(partialChat.getPrincipalRequest());

            if (partialChat.getPerSigla() != null)
                existingChat.setPerSigla(partialChat.getPerSigla());

            if (partialChat.getPeriodPlanilla() != null)
                existingChat.setPeriodPlanilla(partialChat.getPeriodPlanilla());

            if (partialChat.getContractActive() != null)
                existingChat.setContractActive(partialChat.getContractActive());

            return existingChat;
        });
    }

    public boolean validateTime(Chat chat) {
        log.info("Validando chat {}, Fecha inicio: {}, Fecha Authenticación: {}" , chat.getChatId(), chat.getChatStart(), chat.getChatAuthenticated());
        Date lastModified = chat.getChatDateAuthorized();
        if (lastModified == null && validateTimeStart(chat)){
            activeChats.remove(chat.getChatId());
            statusChatServices.delete(chat.getChatId());
        }
        if (lastModified == null) return true;
        Date now = new Date();
        long diffInMillis = now.getTime() - lastModified.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        boolean validate = diffInMinutes > 5;
        if(validate){
            activeChats.remove(chat.getChatId());
            statusChatServices.delete(chat.getChatId());
        }
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


    public QuantityResponse validityQuantityRequest(Action action, Chat chat, String detail, String actionPrincipal ) {
        Integer quantity = action.getActionDaysQuantity();
        if(quantity == null) return new QuantityResponse(null,false);

        Date endDate = new Date();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocalDate = endLocalDate.minusDays(quantity);


        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<QuantityChat> quantityChats = quantityChatServices.findQuantityForDocumentAndAction(
                action.getActionId(), chat.getTypeDocument(), chat.getDocument(), startDate, endDate, detail, actionPrincipal
        );


        if(quantityChats == null || quantityChats.isEmpty())  return new QuantityResponse(null,false);

        LocalDate dateLast = quantityChats.get(0).getAudDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(action.getActionDaysQuantity());

        Boolean isOver = quantityChats.size() >= action.getActionQuantity();
        return new QuantityResponse(dateLast,isOver);
    }

    public void deleteChatId(String chatId){
        activeChats.remove(chatId);
    }

    @Scheduled(fixedRate = 600000) // 600000ms = 10 minutos
    public void validateInactiveChats(){
        log.info("Iniciando borrado de chats.");
        for(Chat chat : activeChats.values()) {
            this.validateTime(chat);
        }
    }



}
