package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IStatusChatDao;
import com.microcode.client.entity.mysql.StatusChat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class StatusChatServices implements StatusChatServicesI {

    private final IStatusChatDao statusChatDao;


    @Override
    public void create(StatusChat statusChat) {
        StatusChat status = statusChatDao.findByChatId(statusChat.getChatId());
        if (status != null) {
            statusChatDao.save(
                    StatusChat.builder()
                            .chatId(statusChat.getChatId())
                            .chatMessage(statusChat.getChatMessage())
                            .chatOptions(statusChat.getChatOptions())
                            .audDate(new Date())
                            .build()
            );
        }else {
            statusChatDao.save(statusChat);
        }
    }

    @Override
    public StatusChat findChatById(String chatId) {
        return statusChatDao.findByChatId(chatId);
    }
}
