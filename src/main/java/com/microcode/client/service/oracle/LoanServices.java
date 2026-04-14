package com.microcode.client.service.oracle;

import com.microcode.client.dao.oracle.ILoanDao;
import com.microcode.client.dao.oracle.ILoanDetailDao;
import com.microcode.client.dao.oracle.ITypePaymentFedacDao;
import com.microcode.client.entity.oracle.Loan;
import com.microcode.client.entity.oracle.LoanDetail;
import com.microcode.client.entity.oracle.TypePaymentFedac;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class LoanServices implements LoanServicesI {

    private final ILoanDao loanDao;
    private final ILoanDetailDao loanDetailDao;
    private final ITypePaymentFedacDao typePaymentFedacDao;
    private final CertificatesServiceI certificatesServiceI;

    @Override
    public void save(Long ctoNumber, String principalTypeDocument, Long principalDocument, String employeeTypeDocument, Long employeeDocument,
                     Long value, Long valueAffiliation, String typePayment, String chatId) {

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() ->
                saveAffiliationInitial(ctoNumber, principalTypeDocument, principalDocument,
                        employeeTypeDocument, employeeDocument,
                        value, valueAffiliation, typePayment, chatId)
        );

        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() ->
                saveAffiliationPayment(ctoNumber, principalTypeDocument, principalDocument,
                        employeeTypeDocument, employeeDocument,
                        value, valueAffiliation, typePayment, chatId)
        );

        CompletableFuture.allOf(f1, f2).join();
    }

    @Override
    public Long getValueAffiliationFedac() {
        return Long.valueOf(typePaymentFedacDao.findTypePaymentFedacByTypePaymentNameAndTypeDocumentAndDocument(
                "UNICO","NI",800141699L
        ).getMetric());
    }

    @Override
    public List<Loan> getIsActive(String typeDocumentPrincipal, Long documentPrincipal, Long ctoNumber, String typeName, String status) {
        return loanDao.findLoanByPrincipalTypeDocumentAndPrincipalDocumentAndCtoNumberAndTypeNameAndStatus(
                typeDocumentPrincipal, documentPrincipal, ctoNumber, typeName, status);
    }


    public void saveAffiliationInitial(Long ctoNumber, String principalTypeDocument, Long principalDocument, String employeeTypeDocument, Long employeeDocument,
                                       Long value, Long valueAffiliation, String typePayment, String chatId){
        TypePaymentFedac typePaymentFedac = typePaymentFedacDao.findTypePaymentFedacByTypePaymentNameAndTypeDocumentAndDocument(
                "UNICO","NI",800141699L
        );

        Loan loanEntity = Loan.builder()
                .principalTypeDocument(principalTypeDocument)
                .principalDocument(principalDocument)
                .ctoNumber(ctoNumber)
                .value(Long.valueOf(typePaymentFedac.getMetric()))
                .valueCapital(Long.valueOf(typePaymentFedac.getMetric()))
                .valueInterests(0L)
                .status("A")
                .typeName("AFILIACION")
                .typePayment("UNICO")
                .warranty("LET")
                .authorization("S")
                .document(ctoNumber.toString())
                .rate(0L)
                .period(1L)
                .height(0L)
                .calculate("S")
                .validity("S")
                .entityTypeDocument("NI")
                .entityDocument(800141699L)
                .typeEntity("PRE")
                .periodicity(typePayment)
                .typeLoan("P")
                .isCont("S")
                .isCheck(0L)
                .control("GEN")
                .date(LocalDateTime.now())
                .commentary("Afiliación TEO Chat ID: " + chatId)
                .providerTypeDocument("NI")
                .providerDocument(800141699L)
                .consecutive(null)
                .dateDev(LocalDateTime.now())
                .employeeTypeDocument(employeeTypeDocument)
                .employeeDocument(employeeDocument)
                .build();

        Loan loan = loanDao.saveAndFlush(loanEntity);

        System.out.println("Afiliacion generado: " + loan);

        LoanDetail loanDetailEntity = LoanDetail.builder()
                .id(loan.getId())
                .process(typePaymentFedac.getProcess())
                .period(typePaymentFedac.getPeriod())
                .consecutive(typePaymentFedac.getConsecutive())
                .dateIni(LocalDateTime.now())
                .dateEnd(LocalDateTime.now().plusYears(10))
                .conceptCap(typePaymentFedac.getCodeCap())
                .conceptInt(typePaymentFedac.getCodeInt())
                .metric(typePaymentFedac.getMetric())
                .metricCap(typePaymentFedac.getMetricCap())
                .metricInt(typePaymentFedac.getMetricInt())
                .value(Long.valueOf(typePaymentFedac.getMetric()))
                .status("A")
                .quotas(typePaymentFedac.getQuotas())
                .height(0L)
                .siglaMpr(typePaymentFedac.getSiglaMpr())
                .dayIni(typePaymentFedac.getDayIni())
                .dayEnd(typePaymentFedac.getDayEnd())
                .dateRange("SI")
                .codeGrb(null)
                .typeDrp("NORMAL")
                .date(null)
                .captAnt(null)
                .observation(null)
                .build();

        LoanDetail loanDetail = loanDetailDao.save(loanDetailEntity);
        System.out.println("Detalle generado: " + loanDetail);

        certificatesServiceI.getPlanPayment(loanDetail.getId());

    }

    public void saveAffiliationPayment(Long ctoNumber, String principalTypeDocument, Long principalDocument, String employeeTypeDocument, Long employeeDocument,
                                       Long value, Long valueAffiliation, String typePayment, String chatId){

        String typePaymentName = this.getSavingsTypeByLetter(typePayment);
        System.out.println("typePaymentName: " + typePaymentName);

        TypePaymentFedac typePaymentFedac = typePaymentFedacDao.findTypePaymentFedacByTypePaymentNameAndTypeDocumentAndDocument(
                typePaymentName,"NI",800141699L
        );

        System.out.println("Tipo ahhorro generado: " + typePaymentFedac);

        Loan loanEntity = Loan.builder()
                .principalTypeDocument(principalTypeDocument)
                .principalDocument(principalDocument)
                .ctoNumber(ctoNumber)
                .value(0L)
                .valueCapital(0L)
                .valueInterests(0L)
                .status("A")
                .typeName("AHORRO")
                .typePayment(typePaymentName)
                .warranty(null)
                .authorization("S")
                .document(ctoNumber.toString())
                .rate(0L)
                .period(0L)
                .height(0L)
                .calculate("S")
                .validity("S")
                .entityTypeDocument("NI")
                .entityDocument(800141699L)
                .typeEntity("PRE")
                .periodicity(typePayment)
                .typeLoan("A")
                .isCont("S")
                .isCheck(null)
                .control("GEN")
                .date(LocalDateTime.now())
                .commentary("Afiliación TEO Chat ID: " + chatId)
                .providerTypeDocument("NI")
                .providerDocument(800141699L)
                .consecutive(0L)
                .dateDev(LocalDateTime.now())
                .employeeTypeDocument(employeeTypeDocument)
                .employeeDocument(employeeDocument)
                .build();

        Loan loan = loanDao.saveAndFlush(loanEntity);

        System.out.println("Ahorro generado: " + loan);

        LoanDetail loanDetailEntity = LoanDetail.builder()
                .id(loan.getId())
                .process(typePaymentFedac.getProcess())
                .period(typePaymentFedac.getPeriod())
                .consecutive(typePaymentFedac.getConsecutive())
                .dateIni(LocalDateTime.now())
                .dateEnd(LocalDateTime.now().plusYears(10))
                .conceptCap(typePaymentFedac.getCodeCap())
                .conceptInt(typePaymentFedac.getCodeInt())
                .metric(typePaymentFedac.getMetric())
                .metricCap(typePaymentFedac.getMetricCap())
                .metricInt(typePaymentFedac.getMetricInt())
                .value(value)
                .status("A")
                .quotas(0L)
                .height(0L)
                .siglaMpr(typePaymentFedac.getSiglaMpr())
                .dayIni(typePaymentFedac.getDayIni())
                .dayEnd(typePaymentFedac.getDayEnd())
                .dateRange("SI")
                .codeGrb(null)
                .typeDrp("NORMAL")
                .date(null)
                .captAnt(null)
                .observation(null)
                .build();

        LoanDetail loanDetail = loanDetailDao.save(loanDetailEntity);
        System.out.println("Ahorro detalle generado: " + loanDetail);

        certificatesServiceI.getCalculatePaymentDetail(loanDetail.getId(), loanDetail.getConsecutive());

    }

//                .period(typePayment.equals("Q") ? 2L : 1L)

    private static final Map<String, String> SAVINGS_TYPE_MAP = Map.of(
            "Q", "QUINC.AHORRO",
            "M", "MENS.AHORRO",
            "C", "CAT.AHORRO",
            "S", "SEM.AHORRO",
            "D", "DEC.AHORRO"
    );

    public String getSavingsTypeByLetter(String letter) {
        if (letter == null || letter.isBlank()) {
            throw new IllegalArgumentException("Letter cannot be null or empty");
        }

        String type = SAVINGS_TYPE_MAP.get(letter.toUpperCase());

        if (type == null) {
            throw new IllegalArgumentException("Unsupported letter for savings type: " + letter);
        }

        return type;
    }

}
