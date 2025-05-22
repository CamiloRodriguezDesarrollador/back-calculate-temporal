package com.microcode.client.service.oracle;

import java.time.LocalDate;
import java.util.List;

public interface ContributionNovServicesI {

    List<String> findPeriodsPay(String tdcTd,Long empNd , String tdcTdEpl, Long eplNd   );

}