package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.entity.oracle.LibIng;

import java.util.List;

public interface LibIngServicesI {

    LibIng findForIdentities(Long empNd, String tdcTd, Long ctoNumber);

}
