package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IHistConstCompanyDao;
import com.microcode.client.dao.oracle.IHistNovCompanyDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistConstantServices implements HistConstantServicesI {

    private final IHistConstCompanyDao histConstCompanyDao;

    @Override
    public Boolean findIfHaveConstant(Long empNdFil) {
        Integer haveConstant = histConstCompanyDao.findIfHaveConstant(empNdFil);
        return haveConstant != null;

    }



}