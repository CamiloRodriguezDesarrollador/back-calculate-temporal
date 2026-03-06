package com.microcode.client.service.manage;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.mysql.Action;

import java.io.IOException;

public interface ManageAdditionalServicesI {

    Object generateCertJob(String detail, Action action, Chat chat) throws IOException;

    Object generateCertPay(String detail, Action action, Chat chat) throws IOException;
    Object generateCertDian(String detail, Action action, Chat chat);
    Object generateDataCCF(String detail, Action action, Chat chat);

    Object generateStatusLiq(String detail, Action action, Chat chat);

    Object generatePlanillaIbc(String detail, Action action, Chat chat);
    Object generateIncapacities(String detail, Action action, Chat chat);
    Object generateCall(String detail, Action action, Chat chat);

    Object generateBienestar(String detail, Action action, Chat chat);

    Object generateCarnet(String detail, Action action, Chat chat);

    Object generatePqr(String detail, Action action, Chat chat) ;

    String assignPrincipalData(Action action, Chat chat);

}
