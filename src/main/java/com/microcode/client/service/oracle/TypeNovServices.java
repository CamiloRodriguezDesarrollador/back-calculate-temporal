package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.ITypeNovCompanyDao;
import com.microcode.client.entity.oracle.TypeNovCompany;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TypeNovServices implements TypeNovServicesI {

    private final ITypeNovCompanyDao tyeNovCompanyDao;

    @Override
    public TypeNovCompany findByIds(Long eplNd, String tdcTd, Long tnoCode) {
        return tyeNovCompanyDao.findByCompanyAndCode(eplNd,tdcTd,tnoCode);
    }
}
