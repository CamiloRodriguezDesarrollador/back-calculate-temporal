package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.PrincipalData;

import java.util.List;

public interface PrincipalDataServicesI {

    String findPrincipal(String principalSigla, Long empNd);
    void create(PrincipalData principalData);
    List<PrincipalData> findPrincipalAll(String principalStatus);

    PrincipalData findPrincipalDataById(Integer principalDataId);
    List<PrincipalData> findTableData(String status, String text, Integer numberPage, Integer numberElementPage);
    Integer findTableQuantity(String status, String text);
    PrincipalData findByEmpNdSigla(Long empNd, String principalSigla);

}
