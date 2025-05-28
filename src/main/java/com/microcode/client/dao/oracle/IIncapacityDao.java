package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Incapacity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IIncapacityDao extends CrudRepository<Incapacity, Long> {

    @Query("""
            SELECT p
            FROM Incapacity p
            WHERE p.empNd = :empNd
            AND p.tdcTd = :tdcTd
            AND p.ctoNumber = :ctoNumber
            AND p.incStatus IN (:incStatus)
            ORDER BY p.incInit DESC
    """)
    List<Incapacity> findIncapacities(Long empNd, String tdcTd, Long ctoNumber, List<String > incStatus);

}