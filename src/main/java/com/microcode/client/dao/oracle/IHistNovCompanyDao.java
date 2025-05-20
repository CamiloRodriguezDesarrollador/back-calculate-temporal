package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.HistNovCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface IHistNovCompanyDao extends CrudRepository<HistNovCompany, Long> {

    @Query("""
            SELECT DISTINCT p.pmgFecini, p.pmgFecFin
            FROM HistNovCompany p
            LEFT JOIN TypeNovCompany e ON p.tdcTd = e.tdcTd AND p.empNd = e.empNd
            WHERE e.tnoCodigo = 11
            AND p.proCodigo NOT IN (16,70,75,51,18)
            AND p.pmgFecini >= :startDate
            AND p.ctoNumero = :ctoNumber AND p.empNd = :empNd AND p.tdcTd = :tdcTd
            ORDER BY p.pmgFecini DESC
    """)
    List<Object[]> findByCompanyAndCode(Long ctoNumber, Long empNd, String tdcTd , LocalDate startDate );

}