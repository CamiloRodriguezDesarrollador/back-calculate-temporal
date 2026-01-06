package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IEmployeeDao;
import com.microcode.client.dao.oracle.IEmployeePhoneDao;
import com.microcode.client.entity.oracle.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeePhoneServices implements EmployeePhoneServicesI {

    private final IEmployeePhoneDao employeePhoneDao;

    @Override
    public List<String> findByIds(Long eplNd, String tdcTd) {
        return employeePhoneDao.findByEplNdAndTdcTd(eplNd, tdcTd);
    }
}
