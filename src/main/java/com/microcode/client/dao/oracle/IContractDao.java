package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IContractDao extends CrudRepository<Contract, Long> {

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd AND p.tdcTdEpl = :tdcTd
            AND p.ectSigla IN ("ACT","RET","PRL")
            AND p.empNd IN (830057687L, 860090915L, 800148972L, 800165661L, 800141699L)
            ORDER BY p.ctoIng DESC
            FETCH FIRST 2 ROWS ONLY
    """)
    List<Contract> findByEplNdAndTdcTdEpl(Long eplNd, String tdcTd);

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd
            AND p.tdcTdEpl = :tdcTd
            AND p.ectSigla = 'ACT'
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findContractActive(Long eplNd, String tdcTd);

    @Query("""
            SELECT p FROM Contract p
            WHERE p.ctoNumero = :ctoNumber
            AND  p.empNd = :empNd
            AND p.tdcTd = :tdcTd
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findForContract(Long ctoNumber,Long empNd, String tdcTd );

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd
            AND p.tdcTdEpl = :tdcTd
            ORDER BY p.ctoIng DESC
            FETCH FIRST 1 ROWS ONLY
    """)
    Contract findForEplLast(Long eplNd, String tdcTd  );


}
