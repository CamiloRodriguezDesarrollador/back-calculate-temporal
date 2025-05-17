package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IHistNovCompanyDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HistNovServices implements HistNovServicesI {

    private final IHistNovCompanyDao histNovCompanyDao;

    @Override
    public List<LocalDate> findPeriodsPay(Long ctoNumber, Long empNd, String tdcTd  ) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(4);
        return histNovCompanyDao.findByCompanyAndCode(ctoNumber,empNd,tdcTd, threeMonthsAgo);
    }
}