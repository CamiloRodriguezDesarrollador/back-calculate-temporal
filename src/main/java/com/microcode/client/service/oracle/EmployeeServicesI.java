package com.microcode.client.service.oracle;

import com.microcode.client.entity.Oracle.Employee;

public interface EmployeeServicesI {

    Employee findByIds(Long eplNd, String tdcTd);

}
