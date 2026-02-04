package com.microcode.client.service.chat;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.QuantityResponse;
import com.microcode.client.entity.mysql.Action;

public interface ChatSessionManagerI {

    String key(String chatId, String companyId);

    void updateChatActivity(String chatId, String companyId, ContentMessage message);


    Chat getChatById(String chatId, String companyId);

    Chat getAuthorizedChatByDocumentAndType(String document, String typeDocument, String companyId);

    boolean isAuthorized(String chatId);

    void setChatById(String chatId, String companyId, Chat partialChat);


    boolean validateTime(Chat chat);

    boolean validateTimeCode(Chat chat);

    boolean validateTimeStart(Chat chat);
    boolean validateAttemptsCode(Chat chat);

    QuantityResponse validityQuantityRequest(Action action, Chat chat, String detail, String actionPrincipal );

    void deleteChatId(String chatId, Integer idCompany);
    void deleteChats();


}
