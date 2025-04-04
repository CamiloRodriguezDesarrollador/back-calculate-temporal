package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Action;

public interface ActionServicesI {

    Action findActionById(Integer actionId);

}
