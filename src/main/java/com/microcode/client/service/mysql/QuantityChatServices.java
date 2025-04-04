package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IQuantityChatDao;
import com.microcode.client.entity.mysql.QuantityChat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuantityChatServices implements QuantityChatServicesI {

    private final IQuantityChatDao quantityChatDao;

    @Override
    public QuantityChat findQuantityForId(Integer quantityId) {
        return quantityChatDao.findByQuantityId(quantityId);
    }

    @Override
    public List<QuantityChat> findQuantityForDocumentAndAction(Integer actionId, String typeDocument,String document) {
        return quantityChatDao.findByActionIdAndTypeDocumentAndDocument(actionId, typeDocument,document);
    }

    @Override
    public void create(QuantityChat quantityChat) {
        quantityChatDao.save(quantityChat);
    }


    public void createForAction(Integer actionId, String typeDocument,String document){
        QuantityChat quantityChat = new QuantityChat();
        quantityChat.setTypeDocument(typeDocument);
        quantityChat.setDocument(document);
        quantityChat.setActionId(actionId);
        this.create(quantityChat);
    }

}
