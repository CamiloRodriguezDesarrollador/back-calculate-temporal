package com.microcode.client.service.mysql;

import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.mysql.RegisterChat;

import java.util.List;

public interface RegisterChatServicesI {

    void create(RegisterChat registerChat);
    void createForMessage(String chatId, ContentMessage contentMessage, String ip, Integer companyId);
    void createForResponse(String chatId, ContentResponse contentResponse, String ip, Integer companyId);
    List<RegisterChat> findTableData(String text, Integer numberPage, Integer numberElementPage);
    Integer findTableQuantity(String text);

}
