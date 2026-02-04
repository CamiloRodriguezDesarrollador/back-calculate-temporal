package com.microcode.client.service.chat;

import com.microcode.client.entity.general.ContentResponse;

public interface ConsumeChatServiceI {

    void sendMessageToChat(String chatId, ContentResponse response);
}