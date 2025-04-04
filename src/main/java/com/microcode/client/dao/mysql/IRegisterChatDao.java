package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.entity.mysql.RegisterChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRegisterChatDao extends CrudRepository<RegisterChat, Long> {

    @Query(""" 
            SELECT p
            FROM RegisterChat p
            WHERE ( lower(p.document) LIKE %:text%  OR lower(p.chatId) LIKE %:text%  )
            ORDER BY p.id DESC
            """)
    List<RegisterChat> findTableData(String text, Pageable pageable);

    @Query(""" 
            SELECT count(p)
            FROM RegisterChat p
            WHERE ( lower(p.document) LIKE %:text%  OR lower(p.chatId) LIKE %:text%  )
            """)
    Integer findTableQuantity(String text);

}
