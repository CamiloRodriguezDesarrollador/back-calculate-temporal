package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IActionDao;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.oracle.ActionsOracleServices;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActionServices implements ActionServicesI {

    private final IActionDao actionDao;

    @Override
    public void create(Action action) {
        actionDao.save(action);
    }

    @Override
    public Action findActionById(Integer actionId) {
        return actionDao.findActionByActionId(actionId);
    }

    @Override
    public List<Action> findByTypeAndStatus(String actionType, String status) {
        return actionDao.findByActionTypeAndActionStatus(actionType,status);
    }

    @Override
    public List<Action> findTableData(String status, String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null) numberPage = 1;
        else if (numberPage<1)  numberPage = 1;
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "actionId");
        return actionDao.findTableData(status,text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String status, String text) {
        return actionDao.findTableQuantity(status, text);
    }

    @Override
    public Action findByMessageAndStatus(String actionMessage, String status) {
        return actionDao.findByActionMessageAndActionStatus(actionMessage, status);
    }

    public void updateTypesChat(){
        List<Action> principal = this.findByTypeAndStatus("principal","A");
        List<Option> optionsPrincipal = new java.util.ArrayList<>(List.of());
        principal.forEach(a -> {optionsPrincipal.add(new Option(a.getActionId(),a.getActionMessage(),null));});
        ActionsOracleServices.updateOptionsPrincipal(optionsPrincipal);


        List<Action> basic = this.findByTypeAndStatus("basic","A");
        List<Option> optionsBasic = new java.util.ArrayList<>(List.of());
        basic.forEach(a -> {optionsBasic.add(new Option(a.getActionId(),a.getActionMessage(),null));});
        ActionsOracleServices.updateOptionsBasic(optionsBasic);

    }

}
