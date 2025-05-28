package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IHistNovCompanyDao;
import com.microcode.client.dao.oracle.IIncapacityDao;
import com.microcode.client.entity.oracle.Incapacity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IncapacityServices implements IncapacityServicesI {

    private final IIncapacityDao incapacityDao;


    @Override
    public List<Incapacity> findIncapacities(Long empNd, String tdcTd, Long ctoNumber, List<String> incStatus) {
        List<Incapacity> all = incapacityDao.findIncapacities(empNd, tdcTd, ctoNumber, incStatus);
        return all.stream().limit(10).toList();
    }

}