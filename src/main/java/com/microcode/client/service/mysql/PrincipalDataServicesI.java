package com.microcode.client.service.mysql;

import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.entity.mysql.RegisterChat;

import java.util.List;

public interface PrincipalDataServicesI {

    String findPrincipal(String principalSigla, Long empNd);
    void create(PrincipalData principalData);

    List<PrincipalData> findPrincipalAll(String principalStatus);

}
