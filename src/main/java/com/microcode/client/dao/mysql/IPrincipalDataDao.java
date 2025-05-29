package com.microcode.client.dao.mysql;

import com.microcode.client.entity.mysql.PrincipalData;
import org.springframework.data.domain.Pageable;
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


    PrincipalData findActionByPrincipalDataId(Integer principalDataId);

    @Query(""" 
            SELECT p
            FROM PrincipalData p
            WHERE ( lower(p.principalValue) LIKE %:text% or lower(CAST(p.empNd AS STRING)) LIKE %:text%
                or lower(p.principalSigla) LIKE %:text% )
            ORDER BY p.principalDataId DESC
            """)
    List<PrincipalData> findTableData(String text, Pageable pageable);

    @Query(""" 
            SELECT count(p)
            FROM PrincipalData p
            WHERE ( lower(p.principalValue) LIKE %:text% or lower(CAST(p.empNd AS STRING)) LIKE %:text%
                or lower(p.principalSigla) LIKE %:text% )
            """)
    Integer findTableQuantity(String text);

    PrincipalData findByEmpNdAndPrincipalSigla(Long empNd, String principalSigla);

}
