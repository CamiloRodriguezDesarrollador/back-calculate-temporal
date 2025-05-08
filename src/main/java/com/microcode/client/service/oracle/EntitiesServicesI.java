package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Company;
import com.microcode.client.entity.oracle.Contract;

import java.util.List;

public interface EntitiesServicesI {

    Company findForDataEpl(Long empNd, String tdcTd, Long ctoNumber, String tenSigla);

}
