package com.microcode.client.service.manage;

import com.microcode.client.clients.ConnectExternalServices;
import com.microcode.client.clients.MailServices;
import com.microcode.client.clients.NotifyServices;
import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.*;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.jasper.JasperServiceI;
import com.microcode.client.service.mysql.*;
import com.microcode.client.service.oracle.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class ManageAdditionalServices implements ManageAdditionalServicesI {

    public static ContentResponse error;

    private final ResponsibleServicesI responsibleServices;
    private final ContractServicesI contractServices;
    private final JasperServiceI jasperService;
    private final EntitiesServicesI entitiesServices;
    private final PrincipalDataServicesI principalDataServices;
    private final IncapacityServicesI incapacityServices;
    private final LibIngServicesI libIngServices;
    private final EmployeePhoneServicesI employeePhoneServices;
    private final CertificatesServiceI certificatesService;

    private final MailServices mailServices;
    private final ConnectExternalServices connectExternalServices;
    private final NotifyServices notifyServices;

    private final OptionsManageService optionsManageService;
    private final HelperService helperService;

    @Override
    public Object generateCertJob(String detail, Action action, Chat chat) throws IOException {
        String[] part = detail.split("-");
        Long contract = Long.parseLong(part[0]);
        Long empNd = Long.parseLong(part[1]);
        String tdcTd = part[2];
        String prom = part[3];

        Contract contJob = contractServices.findForCtoNumber(contract, empNd, tdcTd, chat.getPrincipalRequest());

        byte[] file = jasperService.getCertificateJob(
                contJob.getEmpNd(),
                contJob.getTdcTd(),
                contract,
                prom
        );

        if (file == null) return error;
        file = jasperService.protectPdfWithPassword(file, chat.getDocument());

        String contentMail = String.format(action.getActionRepOkMail(), "Laboral", chat.getNames());
        String subject = String.format(action.getActionRepOkMailSubject(), "Laboral", chat.getNames());

        mailServices.sendMailCertificatesFile(
                contentMail, subject, chat.getChatMail(), file, "CertificadoLaboral.pdf", chat.getPrincipalRequest()
        ).subscribe();

        return null;

    }

    @Override
    public Object generateCertPay(String detail, Action action, Chat chat) throws IOException {
        byte[] filePay = jasperService.getCertificatePay(
                chat.getEmpNd(),
                chat.getTdcTd(),
                chat.getCtoNumber(),
                detail
        );
        if (filePay == null) return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),
                    optionsManageService.getOptionsByActionWithOption(action.getActionOptionError()), action, null);

        filePay = jasperService.protectPdfWithPassword(filePay, chat.getDocument());


        String contentMailPay = String.format(action.getActionRepOkMail(), "de pago", chat.getNames());
        String subjectPay = String.format(action.getActionRepOkMailSubject(), "de pago", chat.getNames());

        mailServices.sendMailCertificatesFile(
                contentMailPay, subjectPay, chat.getChatMail(), filePay, "CertificacionPago.pdf", chat.getPrincipalRequest()
        ).subscribe();
        return null;
    }

    @Override
    public Object generateCertDian(String detail, Action action, Chat chat) {
        if (!helperService.getDateCertificateAvailable()) {
            String message = principalDataServices.getForSiglaAndEmpNd("dianNotDisp", 0L);
            return ContentResponse.buildContentResponseOk(message, OptionsManageService.optionsBasic, action, null);
        }

        Contract contractYear = contractServices.findForYear(
                chat.getEmpNd(), chat.getTdcTd(), chat.getCtoNumber(), helperService.getYearCertificate()
        );

        if (contractYear == null) return action.getActionRespFailMessage();

        String url = certificatesService.getDataCertificatedDian(
                chat.getTdcTd(),
                chat.getEmpNd(),
                chat.getTypeDocument(),
                Long.valueOf(chat.getDocument()),
                helperService.getDateCertifiedDianStartDate(),
                helperService.getDateCertifiedDianEndDate()
        );

        System.out.println(url);
        if (url == null)
            return action.getActionRespFailMessage();

        Mono.fromRunnable(() -> {
                    byte[] certDian = connectExternalServices.connectUrl(url);
                    System.out.println(certDian.length);

                    String contentMailDian = String.format(action.getActionRepOkMail(), "Ingresos y Retenciones", chat.getNames(), url);
                    String subjectDian = String.format(action.getActionRepOkMailSubject(), "Ingresos y Retenciones", chat.getNames());

                    try {
                        mailServices.sendMailCertificatesFile(
                                contentMailDian, subjectDian, chat.getChatMail(), certDian, "IngresosRetenciones.pdf", chat.getPrincipalRequest()
                        ).subscribe();
                    } catch (IOException e) {
                        log.error("ERROR Generar carnet: {}", e.getMessage());
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        return null;
    }

    @Override
    public Object generateDataCCF(String detail, Action action, Chat chat) {
        Company comp = entitiesServices.findForDataEpl(
                chat.getEmpNd(), chat.getTdcTd(), chat.getCtoNumber(), "CCF"
        );

        if (comp == null || chat.getCtoNumber() == null) return ContentResponse.buildContentResponseFail(
                String.format(action.getActionRespFailMessage()),
                optionsManageService.getOptionsByActionWithOption(action.getActionOptionError()),
                action, null
        );
        String nameCompany = comp.getEmpNombre();

        String contentMailCcf = String.format(action.getActionRepOkMail(), chat.getNames(), comp.getEmpNd(), nameCompany);
        String subjectCcf = String.format(action.getActionRepOkMailSubject(), chat.getNames());

        mailServices.sendMailChat(chat.getChatMail(), contentMailCcf, subjectCcf, chat.getPrincipalRequest());

        String mailSend = principalDataServices.getForSiglaAndEmpNd("ccfProvider", comp.getEmpNd());
        return String.format(action.getActionRespOkMessage(), nameCompany, mailSend);
    }

    @Override
    public Object generateStatusLiq(String detail, Action action, Chat chat) {
        String statusLiq = certificatesService.getStatusLiq(
                chat.getTypeDocument(),
                Long.valueOf(chat.getDocument())
        );
        System.out.println(statusLiq);
        if (statusLiq == null)
            return String.format(action.getActionRespFailMessage(), chat.getNames());
        String sigla = HelperService.getDataExtractLine(statusLiq);
        String val = principalDataServices.getForSiglaAndEmpNd(sigla, 0L);

        System.out.println(val);


        if (val == null)
            return String.format(action.getActionRespFailMessage(), chat.getNames());

        if (val.equals("tba")) {
            LibIng libIng = libIngServices.findForIdentities(chat.getEmpNd(), chat.getTdcTd(), chat.getCtoNumber());
            String suc = libIng == null ? "Mas cercana" : libIng.getSucNameAdmin();
            return String.format(val, chat.getNames(), suc);
        }
        return String.format(val, chat.getNames());
    }

    @Override
    public Object generatePlanillaIbc(String detail, Action action, Chat chat) {
        Long typeFormat = detail.contains("Sin IBC") ? 0L : 1L;
        System.out.println(typeFormat);
        System.out.println(chat.getPeriodPlanilla());
        Long codePlanilla = certificatesService.getDataCertificatePlanilla(
                chat.getTdcTd(), chat.getEmpNd(), chat.getTdcTdFil(), chat.getEmpNdFil(), chat.getTypeDocument(), Long.valueOf(chat.getDocument()),
                chat.getPeriodPlanilla(), typeFormat
        );
        System.out.println(codePlanilla);
        if (codePlanilla == null)
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),
                    optionsManageService.getOptionsByActionWithOption(action.getActionOptionError())
                    , action, null);

        Mono.delay(Duration.ofSeconds(10))
                .flatMap(tick -> Mono.fromCallable(() -> {
                    String urlPlanilla = "https://apps.genialw.com/SitioTrabajador/ServletDownloadFileMiPlanilla?TPQ_CODE=" + codePlanilla;
                    byte[] certPlanilla = connectExternalServices.connectUrl(urlPlanilla);

                    String contentMailPlanilla = String.format(action.getActionRepOkMail(), "Planilla", chat.getNames());
                    String subjectPlanilla = String.format(action.getActionRepOkMailSubject(), "Planilla", chat.getNames());

                    mailServices.sendMailCertificatesFile(
                            contentMailPlanilla, subjectPlanilla, chat.getChatMail(), certPlanilla, "Planilla.pdf", chat.getPrincipalRequest()
                    ).subscribe();

                    return true;
                }).subscribeOn(Schedulers.boundedElastic()))
                .subscribe();
        return null;
    }

    @Override
    public Object generateIncapacities(String detail, Action action, Chat chat) {
        List<String> listStatus = detail.equals("Aprobadas") ? List.of("APR") : List.of("CPT", "PEN", "RRI", "RIN");
        List<Incapacity> incapacities = incapacityServices.findIncapacities(
                chat.getEmpNd(),
                chat.getTdcTd(),
                chat.getCtoNumber(),
                listStatus
        );

        System.out.println(incapacities);
        if (incapacities.isEmpty()) {
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), detail.toLowerCase()),
                    optionsManageService.getOptionsByActionWithOption(action.getActionOptionError())
                    , action, null);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String list = """
                    <table style="width: 100%; border: 1px solid #ccc; border-collapse: collapse;" cellpadding="5" cellspacing="0">
                        <thead>
                            <tr>
                                <th style="border: 1px solid #ccc; text-align: center;">Fecha inicio</th>
                                <th style="border: 1px solid #ccc; text-align: center;">Fecha fin</th>
                                <th style="border: 1px solid #ccc; text-align: center;">Días</th>
                                <th style="border: 1px solid #ccc; text-align: center;">Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                    """ +
                    incapacities.stream()
                            .limit(10)
                            .map(i -> String.format(
                                    "<tr><td style='border: 1px solid #ccc; text-align: center; font-size:12px;'>%s</td><td style='border: 1px solid #ccc;  text-align: center; font-size:12px;'>%s</td><td style='border: 1px solid #ccc; text-align: center;font-size:12px;'>%s</td><td style='border: 1px solid #ccc; text-align: center;font-size:12px;'>%s</td></tr>",
                                    formatter.format(i.getIncInit()),
                                    formatter.format(i.getIncEnd()),
                                    i.getIncDays(),
                                    i.getHprCon() == null ? "Pendiente" : "Paga"
                            ))
                            .collect(Collectors.joining()) +
                    """
                                </tbody>
                            </table>
                            """;

            String status;
            if (detail.equals("Pendientes")) {
                String resp;
                Responsible responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
                if (responsible == null) resp = principalDataServices.getForSiglaAndEmpNd("extension", 0L);
                else resp = responsible.getRinMail().toLowerCase();
                status = principalDataServices.getForSiglaAndEmpNd(detail.toLowerCase(), 0L);
                status = String.format(status, resp);
            } else {
                status = principalDataServices.getForSiglaAndEmpNd(detail.toLowerCase(), 0L);
            }
            return String.format(action.getActionRespOkMessage(), list, status);
        }
    }

    @Override
    public Object generateCall(String detail, Action action, Chat chat) {
        List<String> phones;
        try {
            phones = employeePhoneServices.findByIds(
                    Long.valueOf(chat.getDocument()), chat.getTypeDocument());
        } catch (Exception e) {
            phones = null;
        }
        String phonesCsv = phones == null || phones.isEmpty() ? "" : String.join(", ", phones);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentTime = now.format(formatter);

        Map<String, Object> payload = Map.of(
                "Fecha", currentTime,
                "Mensaje", detail,
                "Documento", chat.getDocument(),
                "Nombre", chat.getNames(),
                "Correo", chat.getChatMail(),
                "Telefonos", phonesCsv,
                "Contrato", chat.getContractActive() ? "Activo" : "Retirado",
                "Principal", helperService.defineUniquePrincipalForAuthorizedString(chat.getPrincipalRequest())
        );

        String text =
                "📩 Nueva solicitud de contacto\n\n" +
                        "🕛 *Fecha*: " + currentTime + "\n" +
                        "👤 *Nombre*: " + chat.getNames() + "\n" +
                        "🪪 *Documento*: " + chat.getDocument() + "\n" +
                        "✉️ *Correo*: " + chat.getChatMail() + "\n" +
                        "📞 *Teléfonos*: " + phones + "\n" +
                        "📄 *Estado CTO*: " + (chat.getContractActive() ? "Activo" : "Retirado") + "\n" +
                        "🏛️ *Principal*: " +  helperService.defineUniquePrincipalForAuthorizedString(chat.getPrincipalRequest()) + "\n" +
                        "💬 *Mensaje*: " + detail;

        notifyServices.notifyChatApps(text);
        connectExternalServices.updateDataAppSheetsTeo(payload);
        return null;
    }

    @Override
    public Object generateBienestar(String detail, Action action, Chat chat) {
        if(helperService.isPrincipal(chat.getEmpNdFil())) return String.format(action.getActionRespOkMessagePrincipal());
        String mailResp;
        Responsible responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
        if(responsible == null) {
            mailResp = principalDataServices.getForSiglaAndEmpNd("whatsappBienestar", 0L);
        }
        else {
            mailResp = responsible.getRinMail().toLowerCase();
        };

        mailResp = "<p>Para mayor información, comunícate al </p>" + mailResp;

        return String.format(action.getActionRespOkMessage(),mailResp);
    }

    @Override
    public Object generateCarnet(String detail, Action action, Chat chat) {
        String urlCarnet = certificatesService.getDataCarnetArl(
                chat.getTdcTd(),
                chat.getEmpNd(),
                chat.getCtoNumber()
        );

        System.out.println(urlCarnet);
        if (urlCarnet == null) return action.getActionRespFailMessage();

        Mono.fromRunnable(() -> {
                    byte[] carnet = connectExternalServices.connectUrl(urlCarnet);
                    System.out.println(carnet.length);

                    String contentMailCarnet = String.format(action.getActionRepOkMail());
                    String subjectCarnet = String.format(action.getActionRepOkMailSubject(), chat.getNames());

                    try {
                        mailServices.sendMailCertificatesFile(
                                contentMailCarnet, subjectCarnet, chat.getChatMail(), carnet, "CarnetArl.pdf", chat.getPrincipalRequest()
                        ).subscribe();
                    } catch (IOException e) {
                        log.error("ERROR Generar carnet: {}" , e.getMessage());
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        return null;
    }

    @Override
    public Object generatePqr(String detail, Action action, Chat chat) {
        String sig = chat.getContractActive() ? "A" : "I";
        return principalDataServices.getForSiglaAndEmpNd("pqr" + sig, chat.getEmpNd());
    }


}
