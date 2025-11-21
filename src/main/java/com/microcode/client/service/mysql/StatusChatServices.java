package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IStatusChatDao;
import com.microcode.client.entity.mysql.StatusChat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatusChatServices implements StatusChatServicesI {

    private final IStatusChatDao statusChatDao;


    @Override
    public void create(StatusChat statusChat) {
        statusChatDao.save(statusChat);
    }

    @Override
    public StatusChat findChatById(String chatId) {
        return statusChatDao.findByChatId(chatId);
    }
}
