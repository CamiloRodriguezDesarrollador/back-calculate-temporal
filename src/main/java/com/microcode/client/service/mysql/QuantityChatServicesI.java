package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.QuantityChat;

import java.util.List;

public interface QuantityChatServicesI {

    QuantityChat findQuantityForId(Integer quantityId);
    List<QuantityChat> findQuantityForDocumentAndAction(Integer actionId, String typeDocument,String document);
    void create(QuantityChat quantityChat);
    List<QuantityChat> findTableData(String text, Integer numberPage, Integer numberElementPage);
    Integer findTableQuantity(String text);


}
