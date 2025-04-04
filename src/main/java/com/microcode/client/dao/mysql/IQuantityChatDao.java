package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.entity.mysql.RegisterChat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IQuantityChatDao extends CrudRepository<QuantityChat, Long> {


    QuantityChat findByQuantityId(Integer quantityId);

    List<QuantityChat> findByActionIdAndTypeDocumentAndDocument(Integer actionId
            , String typeDocument,String document);

}
