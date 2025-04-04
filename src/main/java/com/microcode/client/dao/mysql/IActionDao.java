package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.Action;
import org.springframework.data.repository.CrudRepository;

public interface IActionDao extends CrudRepository<Action, Long> {

    Action findActionByActionId(Integer actionId);

}
