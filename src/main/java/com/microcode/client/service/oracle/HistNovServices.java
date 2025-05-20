package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IHistNovCompanyDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistNovServices implements HistNovServicesI {

    private final IHistNovCompanyDao histNovCompanyDao;

    @Override
    public List<String> findPeriodsPay(Long ctoNumber, Long empNd, String tdcTd, Integer quantityList) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(4);
        List<Object[]> periods = histNovCompanyDao.findByCompanyAndCode(ctoNumber, empNd, tdcTd, threeMonthsAgo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return periods.stream()
                .map(row -> {
                    LocalDate ini = (LocalDate) row[0];
                    LocalDate fin = (LocalDate) row[1];
                    return ini.format(formatter) + " - " + fin.format(formatter);
                })
                .distinct()
                .limit(quantityList)
                .collect(Collectors.toList());
    }



}