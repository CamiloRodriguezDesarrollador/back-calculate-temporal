package com.microcode.client.service.oracle;

import java.time.LocalDate;
import java.util.List;

public interface HistNovServicesI {

    List<LocalDate> findPeriodsPay(Long ctoNumber, Long empNd, String tdcTd  );

}