package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.TypePaymentFedac;
import org.springframework.data.repository.CrudRepository;

public interface ITypePaymentFedacDao extends CrudRepository<TypePaymentFedac, Long> {

    TypePaymentFedac findTypePaymentFedacByTypePaymentNameAndTypeDocumentAndDocument(String typePaymentName,String typeDocument,Long document);

}
