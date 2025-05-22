package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IPrincipalDataDao;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.service.oracle.ActionsOracleServices;
import com.microcode.client.service.oracle.OptionsManageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void updateDataPrincipal(){
        List<PrincipalData> actions = principalDataDao.findPrincipalValueAll("A");
        OptionsManageService.updateDataActionsPrincipal(actions);
    }

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

}
