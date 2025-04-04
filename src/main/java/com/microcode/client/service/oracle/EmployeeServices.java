package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IEmployeeDao;
import com.microcode.client.entity.oracle.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServices implements EmployeeServicesI {

    private final IEmployeeDao employeeDao;


    @Override
    public Employee findByIds(Long eplNd, String tdcTd) {
        return employeeDao.findByEplNdAndTdcTd(eplNd,tdcTd);
    }
}
