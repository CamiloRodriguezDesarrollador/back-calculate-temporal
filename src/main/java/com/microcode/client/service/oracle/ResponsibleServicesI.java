package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Responsible;

import java.util.List;

public interface ResponsibleServicesI {

    Responsible findByCompany(String tdcTdFil,  Long empNdFil);

}
