package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.Action;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IActionDao extends CrudRepository<Action, Long> {

    Action findActionByActionId(Integer actionId);

    List<Action> findByActionTypeAndActionStatus(String actionType, String status);

    Action findByActionMessageAndActionStatus(String actionMessage, String status);

    @Query(""" 
            SELECT p
            FROM Action p
            WHERE ( lower(p.actionMessage) LIKE %:text% or lower(p.actionType) LIKE %:text% )
            AND ( lower(p.actionStatus) LIKE %:status% )
            ORDER BY p.actionId DESC
            """)
    List<Action> findTableData(String status, String text, Pageable pageable);

    @Query(""" 
            SELECT count(p)
            FROM Action p
            WHERE ( lower(p.actionMessage) LIKE %:text% or lower(p.actionType) LIKE %:text% )
            AND ( lower(p.actionStatus) LIKE %:status% )
            """)
    Integer findTableQuantity(String status, String text);

}
