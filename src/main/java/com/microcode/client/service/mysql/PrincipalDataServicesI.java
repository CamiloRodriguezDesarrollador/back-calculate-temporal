package com.microcode.client.service.mysql;

import com.microcode.client.entity.mysql.PrincipalData;

import java.util.List;

public interface PrincipalDataServicesI {

    void create(PrincipalData registerChat);

    List<PrincipalData> findPrincipalAll(String principalStatus);

    String findPrincipal(String principalSigla, Long empNd);

    void updateDataPrincipal();

    String getForSiglaAndEmpNd(String sigla, Long empNd );

    PrincipalData findPrincipalDataById(Integer principalDataId);

    PrincipalData findByEmpNdSigla(Long empNd, String principalSigla);

    List<PrincipalData> findTableData(String status, String text, Integer numberPage, Integer numberElementPage);

    Integer findTableQuantity(String status, String text);


}
