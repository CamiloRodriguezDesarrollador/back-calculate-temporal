package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.PrincipalData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface IPrincipalDataDao extends CrudRepository<PrincipalData, Long> {


    @Query(""" 
            SELECT p
            FROM PrincipalData p
            WHERE p.principalStatus = :principalStatus
            """)
    List<PrincipalData> findPrincipalValueAll(String principalStatus);

    @Query(""" 
            SELECT p.principalValue
            FROM PrincipalData p
            WHERE p.principalSigla = :principalSigla
            AND (p.empNd = :empNd OR p.principalDefault = 'y')
            ORDER BY CASE WHEN p.empNd = :empNd THEN 0 ELSE 1 END
            LIMIT 1
            """)
    String findPrincipalValue(String principalSigla, Long empNd);

}
