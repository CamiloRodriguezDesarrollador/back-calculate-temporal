package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.HistConstant;
import com.microcode.client.entity.oracle.HistNovCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface IHistConstCompanyDao extends CrudRepository<HistConstant, Long> {

    @Query("""
            SELECT 1
            FROM HistConstant p
            WHERE p.cteName = 'VERINCSI'
            AND p.hctValue = 'S'
            AND p.empNdFil = :empNdFil
    """)
    Integer findIfHaveConstant(Long empNdFil);

}