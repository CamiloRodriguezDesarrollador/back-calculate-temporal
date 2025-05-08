package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Contract;

import java.util.List;

public interface ContractServicesI {

    List<Contract> findByIds(Long eplNd, String tdcTd);
    Contract findContractActive(Long eplNd, String tdcTd);
    Contract findForCtoNumber(Long ctoNumber,Long empNd, String tdcTd);

}
