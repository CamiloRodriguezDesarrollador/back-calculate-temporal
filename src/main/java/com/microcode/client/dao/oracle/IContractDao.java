package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IContractDao extends CrudRepository<Contract, Long> {

    @Query("""
            SELECT p FROM Contract p
            WHERE p.eplNd = :eplNd AND p.tdcTdEpl = :tdcTd
            AND p.ectSigla IN ("ACT","RET","PRL")
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

}
