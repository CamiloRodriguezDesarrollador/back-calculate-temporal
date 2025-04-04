package com.microcode.client.service.oracle;

import com.microcode.client.entity.Oracle.Contract;

import java.util.List;

public interface ContractServicesI {

    List<String> findByIds(Long eplNd, String tdcTd);

}
