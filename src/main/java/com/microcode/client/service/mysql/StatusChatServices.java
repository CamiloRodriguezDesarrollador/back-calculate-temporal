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
        StatusChat existing = statusChatDao.findByChatIdAndCompanyId(statusChat.getChatId(), statusChat.getCompanyId());
        if (existing != null) {
            if(statusChat.getChatType() != null) existing.setChatType(statusChat.getChatType());
            if(statusChat.getChatAction() != null) existing.setChatAction(statusChat.getChatAction());
            if(statusChat.getChatMessage() != null) existing.setChatMessage(statusChat.getChatMessage());
            if(statusChat.getChatOptions() != null) existing.setChatOptions(statusChat.getChatOptions());
            if(statusChat.getIsHistory() != null) existing.setIsHistory(statusChat.getIsHistory());
//            if(statusChat.getCompanyId() != null) existing.setCompanyId(statusChat.getCompanyId());
            existing.setChatStatus(statusChat.getChatStatus());
            existing.setAudDate(new Date());
            statusChatDao.save(existing);
        } else {
            log.info("Grabando status chat {}", statusChat);
            statusChatDao.save(statusChat);
        }
    }

    @Override
    public void createPend(String chatId, Integer companyId) {
        StatusChat status = StatusChat.builder()
                .chatId(chatId)
                .chatStatus("P")
                .audDate(new Date())
                .companyId(companyId)
                .isHistory("S")
                .build();

        this.create(status);
    }

    @Override
    public void delete(String chatId, Integer companyId) {
        log.info("Entra a delete chat {} {} " , chatId, companyId);
        StatusChat statusChat = findChatById(chatId, companyId);
        if (statusChat != null) {
            statusChatDao.delete(statusChat);
        }
    }


    @Override
    public StatusChat findChatById(String chatId,Integer companyId) {
        return statusChatDao.findByChatIdAndCompanyId(chatId,companyId);
    }
}
