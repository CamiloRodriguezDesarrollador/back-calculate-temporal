package com.microcode.client.dao.oracle;

import com.microcode.client.entity.Oracle.Employee;
import org.springframework.data.repository.CrudRepository;

public interface IEmployeeDao extends CrudRepository<Employee, Long> {

    Employee findByEplNdAndTdcTd(Long eplNd, String tdcTd);

}
