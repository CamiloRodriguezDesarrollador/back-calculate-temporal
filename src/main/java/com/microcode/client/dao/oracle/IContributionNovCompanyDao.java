package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.ContributionParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface IContributionNovCompanyDao extends CrudRepository<ContributionParam, Long> {

    @Query("""
        SELECT 	DISTINCT an.apePeriod
        FROM ContributionParam ap,
             ContributionNov an
        WHERE ap.tdcTd       	 = an.tdcTd
            AND ap.empNd        = an.empNd
            AND ap.apnPeriod   = an.apePeriod
            AND an.apoConsec    = ap.apoConsec
            AND ap.proCodigo    = 300
            AND ap.numPla IS NOT NULL
            AND  to_date(an.apePeriod, 'YYYYMM') >= :startDate
            AND an.tdcTdEpl 	 = :tdcTdEpl
            AND an.eplNd		 = :eplNd
            AND an.tdcTd        = :tdcTd
            AND an.empNd        = :empNd
            ORDER BY an.apePeriod DESC
    """)
    List<String > findPeriods(String tdcTd,Long empNd , String tdcTdEpl, Long eplNd , LocalDate startDate );

}