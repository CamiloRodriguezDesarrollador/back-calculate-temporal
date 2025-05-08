package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Company;
import com.microcode.client.entity.oracle.Contract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IEntitiesDao extends CrudRepository<Company, Long> {

    @Query("""
            SELECT a
            FROM Company a
            LEFT JOIN Entities b
            ON a.empNd = b.empNdEnt AND a.tdcTd = b.tdcTdEnt
            WHERE b.tenSigla = :tenSigla
            AND b.empNd = :empNd
            AND b.tdcTd = :tdcTd
            AND b.ctoNumero = :ctoNumber
    """)
    Company findByEmpNdAndTdcTd(Long empNd, String tdcTd, Long ctoNumber, String tenSigla);


}
