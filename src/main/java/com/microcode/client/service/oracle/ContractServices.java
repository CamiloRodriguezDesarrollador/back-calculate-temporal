package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IContractDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContractServices implements ContractServicesI {

    private final IContractDao ContractDao;


    @Override
    public List<String> findByIds(Long eplNd, String tdcTd) {
        return ContractDao.findByEplNdAndTdcTdEpl(eplNd,tdcTd);
    }
}
