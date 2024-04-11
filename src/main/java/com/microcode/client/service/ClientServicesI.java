package com.microcode.client.service;

import com.microcode.client.entity.Client;

import java.util.List;

public interface ClientServicesI {

    List<Object> findAll();
    List<Client> findTableData(String cliStatus, String text, Integer numberPage, Integer numberElementPage);
    Integer findTableQuantity(String cliStatus, String text);
    List<Client> findFilter(String cliStatus, String text);
    Client findWithCode(String code);
    Client findForIdentity(Integer cliId, String cliStatus);
    Client findForName(String cliName, String cliStatus);
    void create(Client client);
    void updated(Client client);

}
