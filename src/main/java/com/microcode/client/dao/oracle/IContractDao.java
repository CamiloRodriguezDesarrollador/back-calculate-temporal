package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IContractDao extends CrudRepository<Contract, Long> {

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd AND p.tdcTdEpl = :tdcTd
            AND p.ectSigla IN ("ACT","RET","PRL")
            AND p.empNd IN (:principalRequest)
            ORDER BY p.ctoIng DESC
            FETCH FIRST 2 ROWS ONLY
    """)
    List<Contract> findByEplNdAndTdcTdEpl(Long eplNd, String tdcTd, List<Long> principalRequest);

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd
            AND p.tdcTdEpl = :tdcTd
            AND p.ectSigla = 'ACT'
            AND p.empNd IN (:principalRequest)
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findContractActive(Long eplNd, String tdcTd, List<Long> principalRequest);

    @Query("""
            SELECT p FROM Contract p
            WHERE p.ctoNumero = :ctoNumber
            AND  p.empNd = :empNd
            AND p.tdcTd = :tdcTd
            AND p.empNd IN (:principalRequest)
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findForContract(Long ctoNumber,Long empNd, String tdcTd, List<Long> principalRequest );

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd
            AND p.tdcTdEpl = :tdcTd
            AND p.empNd IN (:principalRequest)
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findForEplLast(Long eplNd, String tdcTd , List<Long> principalRequest );


    @Query("""
        SELECT p
        FROM Contract p
        WHERE p.tdcTd = :tdcTd
          AND p.empNd = :empNd
          AND p.ctoNumero = :ctoNumber
          AND p.ectSigla IN ('ACT', 'RET', 'PRL')
          AND (
            p.ctoIng <= TO_DATE(:yearIng || '-12-31', 'YYYY-MM-DD')
            AND (p.ctoEnd IS NULL OR p.ctoEnd >= TO_DATE(:yearIng || '-01-01', 'YYYY-MM-DD'))
          )
        ORDER BY p.ctoIng DESC
        FETCH FIRST 1 ROWS ONLY
    """)
    Contract findByDate(Long empNd, String tdcTd,Long ctoNumber, Integer yearIng);

    @Query(value = """
        SELECT FB_SUELDO(:tdcTd, :empNd, :ctoNumber, :perSigla, SYSDATE)
        FROM dual
    """, nativeQuery = true)
        Long findSalary(String tdcTd, Long empNd, Long ctoNumber, String perSigla);
    }
