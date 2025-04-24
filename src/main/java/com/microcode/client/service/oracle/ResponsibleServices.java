package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IResponsibleDao;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.entity.oracle.Responsible;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ResponsibleServices implements ResponsibleServicesI {

    private final IResponsibleDao responsibleDao;

    @Override
    public String findByCompany(String tdcTdFil,  Long empNdFil) {
        return responsibleDao.findForTdcFilAndEmpNdFil(tdcTdFil,empNdFil);
    }
}
