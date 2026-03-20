package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IContractDao;
import com.microcode.client.entity.oracle.Contract;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContractServices implements ContractServicesI {

    private final IContractDao ContractDao;


    @Override
    public List<Contract> findByIds(Long eplNd, String tdcTd, List<Long> principalRequest) {
        return ContractDao.findByEplNdAndTdcTdEpl(eplNd,tdcTd,principalRequest);
    }

    @Override
    public Contract findContractActive(Long eplNd, String tdcTd, List<Long> principalRequest) {
        return ContractDao.findContractActive(eplNd,tdcTd,principalRequest);
    }

    @Override
    public Contract findForCtoNumber(Long ctoNumber,Long empNd, String tdcTd, List<Long> principalRequest) {
        return ContractDao.findForContract(ctoNumber,empNd,tdcTd,principalRequest);
    }

    @Override
    public Contract findContractForEpl(Long eplNd, String tdcTd, List<Long> principalRequest) {
        return ContractDao.findForEplLast(eplNd,tdcTd,principalRequest);
    }

    @Override
    public Contract findForYear(Long empNd, String tdcTd, Long ctoNumber,Integer yearIng) {
        return ContractDao.findByDate(empNd, tdcTd, ctoNumber,yearIng);
    }

    @Override
    public Long findSalary(String tdcTd, Long empNd, Long ctoNumber, String perSigla) {
        return ContractDao.findSalary(tdcTd, empNd, ctoNumber, perSigla);
    }

}
