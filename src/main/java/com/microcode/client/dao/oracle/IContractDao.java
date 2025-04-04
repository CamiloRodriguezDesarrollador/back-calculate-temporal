package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IContractDao extends CrudRepository<Contract, Long> {

    @Query("""
            SELECT CONCAT(p.ctoNumero,' - Ing: ',p.ctoIng )  FROM Contract p
            WHERE p.eplNd = :eplNd AND p.tdcTdEpl = :tdcTd
            ORDER BY p.ctoIng DESC
            FETCH FIRST 5 ROWS ONLY
    """)
    List<String> findByEplNdAndTdcTdEpl(Long eplNd, String tdcTd);

}
