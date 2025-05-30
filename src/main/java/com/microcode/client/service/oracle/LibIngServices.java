package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IContractDao;
import com.microcode.client.dao.oracle.ILibIngDao;
import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.entity.oracle.LibIng;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LibIngServices implements LibIngServicesI {

    private final ILibIngDao libIngDao;


    @Override
    public LibIng findForIdentities(Long empNd, String tdcTd, Long ctoNumber) {
        return libIngDao.findByIdentities(empNd,tdcTd,ctoNumber);
    }

}
