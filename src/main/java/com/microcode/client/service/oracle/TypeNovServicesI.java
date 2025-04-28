package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.entity.oracle.TypeNovCompany;

import java.util.List;

public interface TypeNovServicesI {

    TypeNovCompany findByIds(Long eplNd, String tdcTd, Long tnoCode);

}
