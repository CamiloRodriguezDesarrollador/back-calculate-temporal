package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Loan;
import com.microcode.client.entity.oracle.LoanDetail;

import java.util.List;

public interface LoanServicesI {

    void save(Long ctoNumber, String principalTypeDocument, Long principalDocument,
                    String employeeTypeDocument, Long employeeDocument, Long value, Long valueAffiliation,
                    String typePayment, String chatId);

    Long getValueAffiliationFedac();

    List<Loan> getIsActive(String typeDocumentPrincipal, Long documentPrincipal, Long ctoNumber, String typeName, String status);

}
