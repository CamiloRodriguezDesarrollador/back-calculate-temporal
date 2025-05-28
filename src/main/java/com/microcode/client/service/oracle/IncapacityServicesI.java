package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.Incapacity;

import java.util.List;

public interface IncapacityServicesI {

    List<Incapacity> findIncapacities(Long empNd, String tdcTd, Long ctoNumber, List<String > incStatus);

}