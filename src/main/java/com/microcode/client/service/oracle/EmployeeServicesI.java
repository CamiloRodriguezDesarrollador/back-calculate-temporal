package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Employee;

public interface EmployeeServicesI {

    Employee findByIds(Long eplNd, String tdcTd);

}
