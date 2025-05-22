package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.IContributionNovCompanyDao;
import com.microcode.client.dao.oracle.IHistNovCompanyDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContributionNovServices implements ContributionNovServicesI {

    private final IContributionNovCompanyDao contributionNovCompanyDao;

    @Override
    public List<String> findPeriodsPay(String tdcTd,Long empNd , String tdcTdEpl, Long eplNd ) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3).withDayOfMonth(1);
        return contributionNovCompanyDao.findPeriods(tdcTd, empNd, tdcTdEpl, eplNd, threeMonthsAgo);
    }



}