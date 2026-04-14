package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ILoanDao extends JpaRepository<Loan, Long> {

    List<Loan> findLoanByPrincipalTypeDocumentAndPrincipalDocumentAndCtoNumberAndTypeNameAndStatus(
            String typeDocumentPrincipal, Long documentPrincipal, Long ctoNumber, String typeName, String status
    );

}
