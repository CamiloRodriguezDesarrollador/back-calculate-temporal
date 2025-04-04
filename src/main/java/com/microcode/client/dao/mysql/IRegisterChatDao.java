package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.RegisterChat;
import org.springframework.data.repository.CrudRepository;

public interface IRegisterChatDao extends CrudRepository<RegisterChat, Long> {


}
