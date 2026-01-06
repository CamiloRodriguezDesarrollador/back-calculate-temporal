package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.EmployeePhone;

import java.util.List;

public interface EmployeePhoneServicesI {

    List<String>  findByIds(Long eplNd, String tdcTd);

}
