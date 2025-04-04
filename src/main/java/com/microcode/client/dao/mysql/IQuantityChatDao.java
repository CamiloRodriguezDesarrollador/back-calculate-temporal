package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.entity.mysql.RegisterChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IQuantityChatDao extends CrudRepository<QuantityChat, Long> {


    QuantityChat findByQuantityId(Integer quantityId);

    List<QuantityChat> findByActionIdAndTypeDocumentAndDocument(Integer actionId
            , String typeDocument,String document);

    @Query(""" 
            SELECT new com.microcode.client.entity.mysql.QuantityChat(
                p.quantityId,p.typeDocument,p.document,p.actionId,p.audDate,pe.actionMessage
            )
            FROM QuantityChat p
            LEFT JOIN Action pe ON pe.actionId = p.actionId
            WHERE ( lower(p.document) LIKE %:text% )
            ORDER BY p.quantityId DESC
            """)
    List<QuantityChat> findTableData(String text, Pageable pageable);


    @Query(""" 
            SELECT count(p)
            FROM QuantityChat p
            WHERE ( lower(p.document) LIKE %:text% )
            """)
    Integer findTableQuantity(String text);

}
