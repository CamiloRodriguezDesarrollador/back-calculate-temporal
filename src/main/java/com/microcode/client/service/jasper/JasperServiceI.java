package com.microcode.client.service.jasper;

import java.io.IOException;

public interface JasperServiceI {

    byte[] getCertificateJob(Long empNd, String tdcTd, Long ctoNumber, String prom);

    byte[] getCertificatePay(Long empNd, String tdcTd, Long ctoNumber, String period);

    byte[] protectPdfWithPassword(byte[] pdfBytes, String password) throws IOException;

}
