package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.StatusChat;

public interface StatusChatServicesI {

    void create(StatusChat statusChat);
    void createPend(String chatId);
    void delete(String chatIdchatId);
    StatusChat findChatById(String chatId);

}
