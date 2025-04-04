package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Action;

import java.util.List;

public interface ActionServicesI {

    void create(Action action);
    Action findActionById(Integer actionId);
    List<Action> findByTypeAndStatus(String actionType, String status);
    List<Action> findTableData(String status, String text, Integer numberPage, Integer numberElementPage);
    Integer findTableQuantity(String status, String text);
    Action findByMessageAndStatus(String actionMessage, String status);

}
