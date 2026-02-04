package com.microcode.client.service.mysql;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.general.Option;
import com.microcode.client.entity.mysql.Action;

import java.util.List;
import java.util.function.Consumer;

public interface ActionServicesI {

    void create(Action action);

    Action findActionById(Integer actionId);

    List<Action> findByTypeAndStatus(String actionType, String status);

    List<Action> findTableData(String status, String text, Integer numberPage, Integer numberElementPage);

    Integer findTableQuantity(String status, String text);

    Action findByMessageAndStatus(String actionMessage, String status);

    void updateTypesChat();

    ContentResponse buildResponse(String actionName, List<Option> secondParam);

    List<Option> updateOptionsByType(String type, Consumer<List<Option>> updater);

    void updateOptionsById( Consumer<List<Option>> updater );

    Action getActionForId(Integer actionId );

    boolean verifiedRequirementContractActive(Chat chat, Action action);
}
