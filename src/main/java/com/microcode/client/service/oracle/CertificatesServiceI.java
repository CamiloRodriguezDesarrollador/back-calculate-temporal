package com.microcode.client.service.oracle;

import com.microcode.client.entity.oracle.CertificateJob;
import com.microcode.client.entity.oracle.CertificatePay;
import com.microcode.client.entity.oracle.RegisterPay;

import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

public interface CertificatesServiceI {

    CertificateJob getDataCertificatedJob(Long nmcteCodigo, Long nmctoNumero, String vcpromediasueldo, String vcdestinatario);

    List<Long> getDataNumbersRads(Long nmctoNumero,Long nmcteCodigo, String vcfechapago );

    CertificatePay getDataCertificatedPay(Long codCertificadoEmpresa, Long numeroContrato,String periodo, Long radicacion) throws SQLException;

    String getDataCertificatedDian(String tdcTd, Long empNd, String tdcTdEpl, Long eplNd, String fechaIncio, String fechaFin);

    List<RegisterPay> getRegisterPays(Array arrPago) throws SQLException;

    String getStatusLiq(String tdcTdEpl,Long eplNd );

    Long getDataCertificatePlanilla(String tdcTd, Long empNd, String tdcTdFil, Long empNdFil, String tdcTdEpl, Long eplNd, String period,
                                           Long typeFormat);

    String getDataCarnetArl(String tdcTd,Long empNd, Long ctoNumero);

    String getPlanPayment(Long loanId);

    String getCalculatePaymentDetail(Long loanId, Long consecutive);

}
