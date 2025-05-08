package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IContractDao;
import com.microcode.client.dao.oracle.IEntitiesDao;
import com.microcode.client.entity.oracle.Company;
import com.microcode.client.entity.oracle.Contract;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EntitiesServices implements EntitiesServicesI {

    private final IEntitiesDao entitiesDao;

    @Override
    public Company findForDataEpl(Long empNd, String tdcTd, Long ctoNumber, String tenSigla) {
        return entitiesDao.findByEmpNdAndTdcTd(empNd,tdcTd,ctoNumber,tenSigla);
    }
}
