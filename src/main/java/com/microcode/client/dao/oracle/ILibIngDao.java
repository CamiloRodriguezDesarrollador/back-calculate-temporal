package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.LibIng;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ILibIngDao extends CrudRepository<LibIng, Long> {


    @Query("""
        SELECT p
        FROM LibIng p
        WHERE p.tdcTdPpal = :tdcTd
          AND p.empNdPpal = :empNd
          AND p.ctoNumero = :ctoNumber
        ORDER BY p.ctoNumero DESC
        FETCH FIRST 1 ROWS ONLY
    """)
    LibIng findByIdentities(Long empNd, String tdcTd,Long ctoNumber);

}
