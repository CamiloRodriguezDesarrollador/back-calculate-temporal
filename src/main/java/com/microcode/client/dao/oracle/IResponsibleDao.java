package com.microcode.client.dao.oracle;

import com.microcode.client.entity.oracle.Responsible;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface IResponsibleDao extends CrudRepository<Responsible, Long> {

    @Query(value = """
        SELECT a
        FROM Responsible a
        LEFT JOIN CompanyAgreement b
        ON a.empAcuCodigo = b.empAcuCodigo
        WHERE a.tcaCodigo = 11
        AND b.tdcTdFil = :tdcTdFil
        AND b.empNdFil = :empNdFil
        AND LOWER(a.rinMail) NOT LIKE 'sat%@activos.com.co'
        AND LOWER(a.rinMail) NOT LIKE 'asesorsat%@activos.com.co'
        AND LOWER(a.rinMail)NOT LIKE 'coordinador%@activos.com.co'
        AND LOWER(a.rinMail) NOT IN(
            'lidersac02@activos.com.co',
            'ipanemasatbarley@activos.com.co'
        )
        ORDER BY a.tcaCodigo
        FETCH FIRST 1 ROWS ONLY

        """)
    Responsible findForTdcFilAndEmpNdFil(String tdcTdFil,  Long empNdFil);

}
