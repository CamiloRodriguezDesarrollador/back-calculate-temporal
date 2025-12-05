package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.StatusChat;
import org.springframework.data.repository.CrudRepository;

public interface IStatusChatDao extends CrudRepository<StatusChat, String> {

    StatusChat findByChatId(String chatId);

}
