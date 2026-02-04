package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IPrincipalDataDao;
import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.service.oracle.OptionsManageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PrincipalDataServices implements PrincipalDataServicesI {

    private final IPrincipalDataDao principalDataDao;

    @Override
    public void create(PrincipalData registerChat) {
        principalDataDao.save(registerChat);
    }

    @Override
    public List<PrincipalData> findPrincipalAll(String principalStatus) {
        return principalDataDao.findPrincipalValueAll(principalStatus);
    }

    @Override
    public String findPrincipal(String principalSigla, Long empNd) {
        return principalDataDao.findPrincipalValue(principalSigla,empNd);
    }

    @Override
    public void updateDataPrincipal(){
        List<PrincipalData> actions = principalDataDao.findPrincipalValueAll("A");
        OptionsManageService.updateDataActionsPrincipal(actions);
    }

    @Override
    public String getForSiglaAndEmpNd(String sigla, Long empNd ){
        String result = OptionsManageService.dataActionsPrincipal.stream()
                .filter(e -> Objects.equals(e.getPrincipalSigla(), sigla) && Objects.equals(e.getEmpNd(), empNd))
                .map(PrincipalData::getPrincipalValue)
                .findFirst()
                .orElse(null);

        if (result == null) {
            result = OptionsManageService.dataActionsPrincipal.stream()
                    .filter(e -> Objects.equals(e.getPrincipalSigla(), sigla) && "y".equals(e.getPrincipalDefault()))
                    .map(PrincipalData::getPrincipalValue)
                    .findFirst()
                    .orElse(null);
        }

        return result;

    }

    @Override
    public PrincipalData findPrincipalDataById(Integer principalDataId) {
        return principalDataDao.findActionByPrincipalDataId(principalDataId);
    }

    @Override
    public PrincipalData findByEmpNdSigla(Long empNd, String principalSigla) {
        return principalDataDao.findByEmpNdAndPrincipalSigla(empNd, principalSigla);
    }



    @Override
    public List<PrincipalData> findTableData(String status, String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null) numberPage = 1;
        else if (numberPage<1)  numberPage = 1;
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "principalDataId");
        return principalDataDao.findTableData(text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String status, String text) {
        return principalDataDao.findTableQuantity(text);
    }


}
