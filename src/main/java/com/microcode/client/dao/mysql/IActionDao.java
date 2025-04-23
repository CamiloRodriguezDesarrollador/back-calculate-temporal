package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.Action;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IActionDao extends CrudRepository<Action, Long> {

    Action findActionByActionId(Integer actionId);

    List<Action> findByActionTypeAndActionStatus(String actionType, String status);

    List<Action> findByActionStatus(String status);

    Action findByActionMessageAndActionStatus(String actionMessage, String status);

    @Query(""" 
            SELECT p
            FROM Action p
            WHERE ( lower(p.actionMessage) LIKE %:text% or lower(p.actionType) LIKE %:text% 
                or lower(p.actionMessage) LIKE %:text% or lower(p.actionRespOkMessage) LIKE %:text% 
                 or lower(p.actionRespFailMessage) LIKE %:text% )
            ORDER BY p.actionId DESC
            """)
    List<Action> findTableData(String text, Pageable pageable);

    @Query(""" 
            SELECT count(p)
            FROM Action p
            WHERE ( lower(p.actionMessage) LIKE %:text% or lower(p.actionType) LIKE %:text% 
                or lower(p.actionMessage) LIKE %:text% or lower(p.actionRespOkMessage) LIKE %:text% 
                 or lower(p.actionRespFailMessage) LIKE %:text% )
            """)
    Integer findTableQuantity(String text);

}
