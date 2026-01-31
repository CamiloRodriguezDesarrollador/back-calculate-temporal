package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.StatusChat;

public interface StatusChatServicesI {

    void create(StatusChat statusChat);
    void createPend(String chatId, Integer companyId);
    void delete(String chatIdchatId, Integer companyId);
    void deleteAll();
    StatusChat findChatById(String chatId,Integer companyId);

}
