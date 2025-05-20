package com.microcode.client.service.oracle;

import java.time.LocalDate;
import java.util.List;

public interface HistNovServicesI {

    List<String> findPeriodsPay(Long ctoNumber, Long empNd, String tdcTd, Integer quantityList  );

}