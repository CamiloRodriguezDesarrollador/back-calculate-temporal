package com.microcode.client.service.mysql;

import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.mysql.RegisterChat;

public interface RegisterChatServicesI {

    void create(RegisterChat registerChat);
    void createForMessage(String chatId, ContentMessage contentMessage, String ip);
    void createForResponse(String chatId, ContentResponse contentResponse, String ip);

}
