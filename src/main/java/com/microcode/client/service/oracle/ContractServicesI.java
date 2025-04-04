package com.microcode.client.service.oracle;

import java.util.List;

public interface ContractServicesI {

    List<String> findByIds(Long eplNd, String tdcTd);

}
