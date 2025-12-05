package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IStatusChatDao;
import com.microcode.client.entity.mysql.StatusChat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class StatusChatServices implements StatusChatServicesI {

    private final IStatusChatDao statusChatDao;


    @Override
    public void create(StatusChat statusChat) {
        StatusChat existing = statusChatDao.findByChatId(statusChat.getChatId());
        if (existing != null) {
            if(statusChat.getChatType() != null) existing.setChatType(statusChat.getChatType());
            if(statusChat.getChatAction() != null) existing.setChatAction(statusChat.getChatAction());
            if(statusChat.getChatMessage() != null) existing.setChatMessage(statusChat.getChatMessage());
            if(statusChat.getChatOptions() != null) existing.setChatOptions(statusChat.getChatOptions());
            if(statusChat.getIsHistory() != null) existing.setIsHistory(statusChat.getIsHistory());
            existing.setChatStatus(statusChat.getChatStatus());
            existing.setAudDate(new Date());
            statusChatDao.save(existing);
        } else {
            log.info("Grabando status chat {}", statusChat);
            statusChatDao.save(statusChat);
        }
    }

    @Override
    public void delete(String chatId) {
        statusChatDao.deleteById(chatId);
    }


    @Override
    public StatusChat findChatById(String chatId) {
        return statusChatDao.findByChatId(chatId);
    }
}
