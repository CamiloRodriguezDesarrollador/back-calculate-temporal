package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Contract;

import java.util.List;

public interface ContractServicesI {

    Long findSalary(String tdcTd, Long empNd, Long ctoNumber, String perSigla);
    List<Contract> findByIds(Long eplNd, String tdcTd, List<Long> principalRequest);
    Contract findContractActive(Long eplNd, String tdcTd, List<Long> principalRequest);
    Contract findContractForEpl(Long eplNd, String tdcTd, List<Long> principalRequest);
    Contract findForCtoNumber(Long ctoNumber,Long empNd, String tdcTd, List<Long> principalRequest);
    Contract findForYear(Long empNd, String tdcTd, Long ctoNumber ,Integer yearIng);

}
