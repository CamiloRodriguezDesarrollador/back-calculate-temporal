package com.microcode.client.service;

import com.microcode.client.dao.IClientDao;
import com.microcode.client.entity.Client;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServices implements ClientServicesI {

    private final IClientDao clientDao;

    @Override
    public List<Object> findAll() {
        return clientDao.findByAll("A");
    }

   @Override
    public List<Client> findTableData(String cliStatus, String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null){
            numberPage = 1;
        }else if (numberPage<1){
            numberPage = 1;
        }
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "cliId");
        return clientDao.findTableData(cliStatus,text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String cliStatus, String text) {
        return clientDao.findTableQuantity(cliStatus, text);
    }

    @Override
    public List<Client> findFilter(String cliStatus, String text) {
        return clientDao.findWithFilter(cliStatus,text);
    }

    @Override
    public Client findWithCode(String code) {
        return clientDao.findByCliCode(code);
    }

    @Override
    public Client findForIdentity(Integer cliId, String cliStatus) {
        return clientDao.findByCliIdAndCliStatus(cliId,cliStatus);
    }

    @Override
    public Client findForName(String cliName, String cliStatus) {
        return clientDao.findByCliNameAndCliStatus(cliName.toLowerCase(), cliStatus);
    }

    @Override
    public void create(Client client) {
        clientDao.save(client);
    }

    @Override
    public void updated(Client client) {
        clientDao.save(client);
    }
}
