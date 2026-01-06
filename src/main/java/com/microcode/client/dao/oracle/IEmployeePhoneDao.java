package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.entity.oracle.EmployeePhone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEmployeePhoneDao extends CrudRepository<EmployeePhone, Long> {

    @Query("""
        SELECT 	DISTINCT emp.number
        FROM EmployeePhone emp
        WHERE emp.eplNd		 = :eplNd
            AND emp.tdcTd        = :tdcTd
    """)
    List<String> findByEplNdAndTdcTd(Long eplNd, String tdcTd);

}
