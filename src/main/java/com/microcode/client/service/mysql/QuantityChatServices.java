package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IQuantityChatDao;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.QuantityChat;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public  List<QuantityChat> findQuantityForDocumentAndAction(Integer actionId, String typeDocument, String document, Date startDate, Date endDate, String actionDetail, String actionPrincipal) {
        return quantityChatDao.findByActionIdAndTypeDocumentAndDocument(actionId, typeDocument,document,startDate,endDate,actionDetail, actionPrincipal);
    }

    @Override
    public List<QuantityChat> findQuantityForDocument(String typeDocument, String document, Date startDate, Date endDate, String actionDetail,String actionPrincipal) {
        return quantityChatDao.findByTypeDocumentAndDocument(typeDocument,document,startDate,endDate,actionDetail, actionPrincipal);
    }

    @Override
    public void create(QuantityChat quantityChat) {
        quantityChatDao.save(quantityChat);
    }

    @Override
    public List<QuantityChat> findTableData(String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null) numberPage = 1;
        else if (numberPage<1)  numberPage = 1;
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "quantityId");
        return quantityChatDao.findTableData(text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String text) {
        return quantityChatDao.findTableQuantity( text);
    }

    public void createForAction(Integer actionId, String typeDocument,String document, String detail, String actionPrincipal){
        QuantityChat quantityChat = new QuantityChat();
        quantityChat.setTypeDocument(typeDocument);
        quantityChat.setDocument(document);
        quantityChat.setActionId(actionId);
        quantityChat.setActionPrincipal(actionPrincipal);
        quantityChat.setActionDetail(detail);
        this.create(quantityChat);
    }

    public void createQuantityForAction(Action action, Chat chat, String detail,String actionPrincipal){
        if(action.getActionQuantity() != null && action.getActionDaysQuantity() != null){
            createForAction(
                    Integer.valueOf(action.getActionId().toString()),
                    chat.getTypeDocument(),
                    chat.getDocument(),
                    detail,
                    actionPrincipal
            );
        }
    }







}
