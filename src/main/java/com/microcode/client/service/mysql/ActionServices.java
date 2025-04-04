package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IActionDao;
import com.microcode.client.entity.mysql.Action;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActionServices implements ActionServicesI {

    private final IActionDao actionDao;

    @Override
    public Action findActionById(Integer actionId) {
        return actionDao.findActionByActionId(actionId);
    }

}
