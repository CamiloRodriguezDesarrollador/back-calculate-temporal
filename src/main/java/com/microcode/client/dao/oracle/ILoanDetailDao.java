package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Loan;
import com.microcode.client.entity.oracle.LoanDetail;
import org.springframework.data.repository.CrudRepository;

public interface ILoanDetailDao extends CrudRepository<LoanDetail, Long> {


}
