package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.TypeNovCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ITypeNovCompanyDao extends CrudRepository<TypeNovCompany, Long> {

    @Query("""
            SELECT p FROM TypeNovCompany p
            WHERE p.empNd = :empNd AND p.tdcTd = :tdcTd
            AND p.tnoCodigo = :tnoCode
    """)
    TypeNovCompany findByCompanyAndCode(Long empNd, String tdcTd , Long tnoCode );

}