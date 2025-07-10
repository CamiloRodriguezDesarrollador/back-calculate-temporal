package com.microcode.client.service.oracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcode.client.clients.ConnectExternalServices;
import com.microcode.client.clients.MailServices;
import com.microcode.client.entity.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.*;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.Option;
import com.microcode.client.service.chat.ConsumeChatService;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.jasper.JasperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.PrincipalDataServices;
import com.microcode.client.service.mysql.QuantityChatServices;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Setter
@Getter
public class ActionsOracleServices {

    private final EmployeeServices employeeService;
    private final ResponsibleServices responsibleServices;
    private final ContractServices contractServices;
    private final ChatSessionManager chatSessionManager;
    private final QuantityChatServices quantityChatServices;
    private final JasperService jasperService;
    private final MailServices mailServices;
    private final EntitiesServices entitiesServices;
    private final CertificatesService certificatesService;
    private final HelperService helperService;
    private final ActionServices actionServices;

    public static ContentResponse unauthorized;
    public static ContentResponse notFound;
    public static ContentResponse quantityMax;
    public static ContentResponse noAction;
    public static ContentResponse timeOut;
    public static ContentResponse error;
    public static ContentResponse withoutContract;
    public static ContentResponse maxAttempts;
    public static ContentResponse otherSessionActive;
    private final HistNovServices histNovServices;
    private final ConsumeChatService consumeChatService;
    private final ContributionNovServices contributionNovServices;
    private final OptionsManageService optionsManageService;
    private final PrincipalDataServices principalDataServices;
    private final HistConstantServices histConstantServices;
    private final IncapacityServices incapacityServices;
    private final LibIngServices libIngServices;
    private final ConnectExternalServices connectExternalServices;

    @PostConstruct
    public void init() {
        actionServices.updateTypesChat();
        principalDataServices.updateDataPrincipal();
    }

    public final String MAIL_TEST = "cgonzalez@activos.com.co";

    public Chat initialChatIfNull(String chatId){
        chatSessionManager.updateChatActivity(chatId,null);
        return chatSessionManager.getChatById(chatId);
    }

    public ContentResponse getDataUserExternal(Map<String,String> inputs, Action action) throws JsonProcessingException {
        try{

            String typeDocument = inputs.get("typeIdentity");
            String document = inputs.get("identity");
            String name = inputs.get("name");
            String phone = inputs.get("phone");
            String email = inputs.get("email");
            String chatId = inputs.get("chatId");
            String principalRequest = inputs.get("principalRequest");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> idsPrincipal = objectMapper.readValue(principalRequest, new TypeReference<>() {});

            Chat chat;

            chat = chatSessionManager.getChatById(chatId);
            if(chat == null) chat = initialChatIfNull(chatId);
            chat.setChatStart(new Date());
            chat.setNames(name);
            chat.setDocument(document);
            chat.setTypeDocument(typeDocument);
            chat.setChatMail(email);
            chat.setChatAuthenticated(false);
            chat.setPrincipalRequest(idsPrincipal);
            chat.setEmpNd(idsPrincipal.get(0));
            chat.setTdcTd("NI");



            if(name != null && phone != null && email != null && document != null){
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),OptionsManageService.optionsPrincipalExternal, action);
            }
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),OptionsManageService.optionsBasicExternal, action);
        } catch (Exception e) {
            return ActionsOracleServices.responseWithOptionsParam(error,action);
        }

    }


    public ContentResponse getDataUser(Map<String,String> inputs, Action action) {
        try{
            String typeDocument = inputs.get("typeDocument");
            String document = inputs.get("document");
            String chatId = inputs.get("chatId");
            String principalRequest = inputs.get("principalRequest");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> idsPrincipal = objectMapper.readValue(principalRequest, new TypeReference<>() {});

            Chat chat;

            chat = chatSessionManager.getChatById(chatId);
            if(chat == null) chat = initialChatIfNull(chatId);

            if (typeDocument == null || document == null) return notFound;

            chat.setChatStart(new Date());

            Long docSearch = Long.valueOf(document);
            Employee employee = employeeService.findByIds(docSearch,typeDocument);
            if (employee == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), typeDocument, document),null, action);
            List<Contract> contracts = contractServices.findByIds(employee.getEplNd(), employee.getTdcTd(),idsPrincipal);

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), typeDocument, document),null, action);


            if(employee.getEmail() == null) return ContentResponse.buildNotMail(null);
            if(employee.getEmpLastName() == null && employee.getEmplName() == null )
                return ContentResponse.buildNotMail(null);

            Contract contract = contractServices.findContractActive(employee.getEplNd() ,employee.getTdcTd(),idsPrincipal);

            String firstName = employee.getEmplName() != null ? employee.getEmplName() : "";
            String lastName = employee.getEmpLastName() != null ? employee.getEmpLastName() : "";
            chat.setNames(firstName + " " + lastName);
            chat.setDocument(document);
            chat.setTypeDocument(typeDocument);
            chat.setChatMail(employee.getEmail());
            chat.setChatAuthenticated(false);
            chat.setPrincipalRequest(idsPrincipal);
            if (contract != null) {
                chat.setEmpNd(contract.getEmpNd());
                chat.setTdcTd(contract.getTdcTd());
                chat.setEmpNdFil(contract.getEmpNdFil());
                chat.setTdcTdFil(contract.getTdcTdFil());
                chat.setCtoNumber(contract.getCtoNumero());
                chat.setPerSigla(contract.getPerSigla());
                chat.setContractActive(true);
                chat.setEmpNdFil(contract.getEmpNdFil());

            }else{
                Contract cont = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());
                chat.setEmpNd(cont.getEmpNd());
                chat.setTdcTd(cont.getTdcTd());
                chat.setEmpNdFil(cont.getEmpNdFil());
                chat.setTdcTdFil(cont.getTdcTdFil());
                chat.setCtoNumber(cont.getCtoNumero());
                chat.setPerSigla(cont.getPerSigla());
                chat.setContractActive(false);
                chat.setEmpNdFil(cont.getEmpNdFil());

            }

            chatSessionManager.setChatById( chatId, chat );

            String mailUser = helperService.generateMail(employee.getEmail().toLowerCase());
            mailServices.ping();
            connectExternalServices.ping();
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), mailUser),null, action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;

        }
    }

    public ContentResponse verifiedMail(Map<String,String> inputs, Action action) {
        try {
            String isMailCorrect = inputs.get("isMailCorrect");
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            chat.setChatStart(new Date());

            if (isMailCorrect.equals("Y")) {
//                String code = "123456";
                String code = helperService.generateCode();
                chat.setChatCode(code);
                chat.setChatAttempts(1);
                chat.setChatDateCode(new Date());

                String contentMail = String.format(action.getActionRepOkMail(),code);
                String subject = String.format(action.getActionRepOkMailSubject(),code);

                mailServices.sendMailChat(MAIL_TEST,contentMail,subject,chat.getPrincipalRequest());
//                mailServices.sendMailChat(chat.getChatMail(),contentMail,subject,chat.getPrincipalRequest());

                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()), null, action);
            }

            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), null, action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    public ContentResponse verified(Map<String,String> inputs, Action action) {
        try{

            String codeVerified = inputs.get("codeVerification");
            String chatId = inputs.get("chatId");

            Chat chat = chatSessionManager.getChatById(chatId);
            if(chatSessionManager.validateTimeCode(chat)) return timeOut;

            if(codeVerified.toLowerCase().equals(chat.getChatCode()) ){

                try{
                    Chat chatLast = chatSessionManager.getAuthorizedChatByDocumentAndType(chat.getDocument(),chat.getTypeDocument());
                    if(chatLast != null) {
                        chatLast.setChatAuthenticated(false);
                        chatLast.setChatDateAuthorized(null);
                        consumeChatService.sendMessageToChat(chatLast.getChatId(), otherSessionActive);
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                chat.setChatAuthenticated(true);
                chat.setChatDateAuthorized(new Date());
                chatSessionManager.setChatById( chatId, chat );
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),OptionsManageService.optionsPrincipal, action);

            }

            if(chatSessionManager.validateAttemptsCode(chat)) return maxAttempts;

            chat.setChatAttempts(chat.getChatAttempts()+1);
            int att = chat.getChatAttempts()-4;
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(),att*-1),null, action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse getCertifiedJob(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            List<Contract> contracts = contractServices.findByIds(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),OptionsManageService.optionsDocument, action);

            List<Option> options = new ArrayList<>(List.of());
            List<String> labels = List.of("Último contrato", "Contrato Anterior");

            for (int i = 0; i < contracts.size() && i < 2; i++) {
                var contract = contracts.get(i);
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                labels.get(i),
                                contract.getCtoNumero()+"-"+contract.getEmpNd()+"-"+contract.getTdcTd(),
                                contract.getCtoNumero()+"-"+contract.getEmpNd()+"-"+contract.getTdcTd()
                        )
                );
            }

            Action actionBack = actionServices.getActionForId(1004);
            options.add(
                    new Option(
                            actionBack.getActionRespOkAction(),
                            actionBack.getActionMessage(),
                            null,
                            null
                    )
            );

            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse getCertifiedPay(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            Integer quantityPeriods = chat.getPerSigla().equals("M") ? 3 : 6;
            List<String> dates = histNovServices.findPeriodsPay(
                    chat.getCtoNumber(),chat.getEmpNd(),chat.getTdcTd(),quantityPeriods
            );

            return getContentResponse(action, chat, dates,1004);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse getCertifiedPlanilla(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;
            if(chat.getContractActive() == null ||  !chat.getContractActive()) return responseWithOptionsParam(withoutContract,action);

            List<String> dates = contributionNovServices.findPeriodsPay(
                    chat.getTdcTd(),chat.getEmpNd(),chat.getTypeDocument(), Long.valueOf(chat.getDocument())
            );

            return getContentResponse(action, chat, dates,1004);
        } catch (Exception e) {
            return error;
        }

    }

    public ContentResponse getCertifiedPlanillaIbc(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            String detail = inputs.get("detail");

            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            if(detail == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), chat.getNames()),OptionsManageService.optionsDocument, action);

            chat.setPeriodPlanilla(detail);

            List<Option> options = new ArrayList<>(List.of());

            for (String label : List.of("Sin IBC","Con IBC")) {
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                label,
                                label,
                                label
                        )
                );
            }

            Action actionBack = actionServices.getActionForId(504);
            options.add(
                    new Option(
                            actionBack.getActionId(),
                            "Atras 🚪",
                            null,
                            null
                    )
            );

            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
        } catch (Exception e) {
            return error;
        }

    }

    public ContentResponse inactiveChat(Map<String,String> inputs, Action action) {
        try{
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            chat.setChatAuthenticated(false);
            chatSessionManager.setChatById(chatId,chat);
            return timeOut;
        } catch (Exception e) {
            return error;
        }
    }

    public ContentResponse getInformationIncaStatus(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;
            if(chat.getContractActive() == null ||  !chat.getContractActive()) return responseWithOptionsParam(withoutContract,action);

            if(helperService.isPrincipal(chat.getEmpNdFil())){

                ContentResponse validateQuantity = validateQuantityOver(action, chat, "0", chat.getEmpNd().toString());
                if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

                String messageOk = principalDataServices.getForSiglaAndEmpNd("plantaIncapacidad", 0L);
                String val = principalDataServices.getForSiglaAndEmpNd("urlSiteJob", chat.getEmpNd());

                quantityChatServices.createQuantityForAction(action,chat,"0", chat.getEmpNd().toString());

                return ContentResponse.buildContentResponseOk(
                        String.format(messageOk,val),
                        OptionsManageService.optionsBasic,
                        action
                );
            }

            List<String> options = List.of("Aprobadas","Pendientes");
            return getContentResponse(action, chat, options,1008);
        } catch (Exception e) {
            return error;
        }

    }



//   Metodo estandar

    public ContentResponse methodStandardRedirect(Map<String,String> inputs, Action action) {
        try{
            String chatId = inputs.get("chatId");
            String typeChat = inputs.get("typeChat");
            Chat chat = chatSessionManager.getChatById(chatId);

            if(typeChat.equals("1")) {
                ContentResponse resp = this.validateInitial(chat);
                if(resp != null) return resp;
            }

            return ContentResponse.buildContentResponseOk(
                    String.format(action.getActionRespOkMessage(), chat.getNames()),
                    OptionsManageService.getOptionsByActionWithName(action.getActionOption()),
                    action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ActionsOracleServices.responseWithOptionsParam(error,action);
        }
    }

    public ContentResponse methodStandard(Map<String,String> inputs, Action actionOriginal) {
        try {
            Action action = actionOriginal.clone();
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            String typeChat = inputs.get("typeChat");

            Chat chat = chatSessionManager.getChatById(chatId);

            if(!actionServices.verifiedRequirementContractActive(chat,action)) return withoutContract;

            String messageOk = helperService.isPrincipal(chat.getEmpNdFil()) ? action.getActionRespOkMessagePrincipal() : action.getActionRespOkMessage();

            ContentResponse validateQuantity = validateQuantityOver(action, chat, detail,chat.getEmpNd().toString());

            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            if(typeChat.equals("1")){
                ContentResponse resp = this.validateInitial(chat);
                if (resp != null) return resp;
            }

            if(action.getActionSigla() != null || action.getActionSiglaPrincipal() != null){
                messageOk = assignPrincipalData(action,chat);
            }

            Object validationAdditional = methodStandardAdditional(detail, action,chat);

            if(validationAdditional != null){
                if(validationAdditional instanceof String){
                    messageOk = (String) validationAdditional;
                }
                if(validationAdditional instanceof ContentResponse){

                    return (ContentResponse) validationAdditional;
                }
            }

            quantityChatServices.createQuantityForAction(action,chat,detail,chat.getEmpNd().toString());

            return ContentResponse.buildContentResponseOk(
                    messageOk,
                    OptionsManageService.getOptionsByActionWithName(action.getActionOption()),
                    action
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ActionsOracleServices.responseWithOptionsParam(error,actionOriginal);
        }
    }


    public Object methodStandardAdditional(String detail, Action action, Chat chat) {
        try{
            switch (action.getActionId()) {
                case 502:
                        String[] part = detail.split("-");
                        Long contract = Long.parseLong(part[0]);
                        Long empNd = Long.parseLong(part[1]);
                        String tdcTd = part[2];

                        Contract contJob = contractServices.findForCtoNumber(contract, empNd, tdcTd, chat.getPrincipalRequest());

                        byte[] file = jasperService.getCertificateJob(
                                contJob.getEmpNd(),
                                contJob.getTdcTd(),
                                contract
                        );

                        if (file == null) return error;
                        file = jasperService.protectPdfWithPassword(file, chat.getDocument());

                        String contentMail = String.format(action.getActionRepOkMail(),"Laboral", chat.getNames());
                        String subject = String.format(action.getActionRepOkMailSubject(),"Laboral", chat.getNames());

                        mailServices.sendMailCertificatesFile(
                                contentMail, subject, chat.getChatMail(), file, "CertificadoLaboral.pdf",chat.getPrincipalRequest()
                        ).subscribe();
                    return null;

                case 528:
                        byte[] filePay = jasperService.getCertificatePay(
                                chat.getEmpNd(),
                                chat.getTdcTd(),
                                chat.getCtoNumber(),
                                detail
                        );
                        if (filePay == null)
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
                        filePay = jasperService.protectPdfWithPassword(filePay, chat.getDocument());


                        String contentMailPay = String.format(action.getActionRepOkMail(),"de pago", chat.getNames());
                        String subjectPay = String.format(action.getActionRepOkMailSubject(),"de pago", chat.getNames());

                        mailServices.sendMailCertificatesFile(
                                contentMailPay, subjectPay, chat.getChatMail(), filePay, "CertificacionPago.pdf",chat.getPrincipalRequest()
                        ).subscribe();
                    return null;

                case 505:
                        if(!helperService.getDateCertificateAvailable()){
                            String message = principalDataServices.getForSiglaAndEmpNd("dianNotDisp", 0L);
                            return ContentResponse.buildContentResponseOk(message, OptionsManageService.optionsBasic, action);
                        }

                        Contract contractYear =  contractServices.findForYear(
                                chat.getEmpNd(),chat.getTdcTd(),chat.getCtoNumber(),helperService.getYearCertificate()
                        );

                        if(contractYear == null) return action.getActionRespFailMessage();

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
                                            return;
                                        }
                                    })
                            .subscribeOn(Schedulers.boundedElastic())
                        .subscribe();

                        return null;

                case 529:
                    Company comp = entitiesServices.findForDataEpl(
                            chat.getEmpNd(), chat.getTdcTd(), chat.getCtoNumber(), "CCF"
                    );

                    if(comp == null || chat.getCtoNumber() == null) return ContentResponse.buildContentResponseFail(
                            String.format(action.getActionRespFailMessage()),
                            OptionsManageService.optionsCCF,
                            action
                    );
                    String nameCompany = comp.getEmpNombre();

                    String contentMailCcf = String.format(action.getActionRepOkMail(),chat.getNames(),comp.getEmpNd(),nameCompany);
                    String subjectCcf = String.format(action.getActionRepOkMailSubject(),chat.getNames());

                    mailServices.sendMailChat(chat.getChatMail(),contentMailCcf,subjectCcf,chat.getPrincipalRequest());

                    String mailSend = principalDataServices.getForSiglaAndEmpNd("ccfProvider", comp.getEmpNd());
                    return String.format(action.getActionRespOkMessage(),nameCompany,mailSend);

                case 101 :
                    String statusLiq = certificatesService.getStatusLiq(
                            chat.getTypeDocument(),
                            Long.valueOf(chat.getDocument())
                    );
                    System.out.println(statusLiq);
                    if(statusLiq == null)
                        return  String.format(action.getActionRespFailMessage(),chat.getNames() );
                    String sigla = HelperService.getDataExtractLine(statusLiq);
                    String val = principalDataServices.getForSiglaAndEmpNd(sigla, 0L);

                    System.out.println(val);


                    if(val == null)
                        return String.format(action.getActionRespFailMessage(),chat.getNames());

                    if(val.equals("tba")){
                        LibIng libIng = libIngServices.findForIdentities(chat.getEmpNd(),chat.getTdcTd(),chat.getCtoNumber());
                        String suc = libIng == null ? "Mas cercana" : libIng.getSucNameAdmin();
                        return  String.format(val,chat.getNames(),suc);
                    }
                    return String.format(val,chat.getNames() );

                case 535 :
                        Long typeFormat = detail.equals("Sin IBC") ? 0L : 1L;
                        System.out.println(typeFormat);
                        System.out.println(chat.getPeriodPlanilla());
                        Long codePlanilla = certificatesService.getDataCertificatePlanilla(
                                chat.getTdcTd(), chat.getEmpNd(), chat.getTdcTdFil(), chat.getEmpNdFil(), chat.getTypeDocument(), Long.valueOf(chat.getDocument()),
                                chat.getPeriodPlanilla(), typeFormat
                        );
                        System.out.println(codePlanilla);
                        if (codePlanilla == null)
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
                        //


                        Mono.delay(Duration.ofSeconds(10))
                                .flatMap(tick -> Mono.fromCallable(() -> {
                                    String urlPlanilla = "https://apps.genialw.com/SitioTrabajador/ServletDownloadFileMiPlanilla?TPQ_CODE=" +codePlanilla;
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

                case 537 :
                        List<String> listStatus = detail.equals("Aprobadas") ? List.of("APR") :  List.of("CPT","PEN","RRI","RIN");
                        List<Incapacity> incapacities = incapacityServices.findIncapacities(
                                chat.getEmpNd(),
                                chat.getTdcTd(),
                                chat.getCtoNumber(),
                                listStatus
                        );

                        System.out.println(incapacities);
                        if(incapacities.isEmpty()){
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(),detail.toLowerCase()), OptionsManageService.optionsInca, action);
                        }else{
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
                                                    i.getHprCon()  == null ? "Pendiente" : "Paga"
                                            ))
                                            .collect(Collectors.joining()) +
                                    """
                                        </tbody>
                                    </table>
                                    """;

                            String status;
                            if(detail.equals("Pendientes")){
                                String responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
                                if(responsible == null) responsible = principalDataServices.getForSiglaAndEmpNd("extension", 0L);
                                status = principalDataServices.getForSiglaAndEmpNd(detail.toLowerCase(), 0L);
                                status = String.format(status,responsible);
                            }else{
                                status = principalDataServices.getForSiglaAndEmpNd(detail.toLowerCase(), 0L);
                            }
                            return String.format(action.getActionRespOkMessage(),list,status);
                        }
                case 105 :
                    String sig = chat.getContractActive() ? "A" : "I";
                    return principalDataServices.getForSiglaAndEmpNd("pqr"+sig, chat.getEmpNd());

            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return error;
        }

    }

//    Validations

    public ContentResponse validateInitial(Chat chat){
        if(chat == null || !chat.getChatAuthenticated() ) return unauthorized;
        if(chatSessionManager.validateTime(chat) || !chat.getChatAuthenticated()) return timeOut;
        chat.setChatDateAuthorized(new Date());
        return null;
    }

    public ContentResponse validateQuantityOver(Action action, Chat chat, String detail, String actionPrincipal){
        if(action.getActionQuantity() == null || action.getActionDaysQuantity() == null) return null;
        QuantityResponse quantityResponse = chatSessionManager.validityQuantityRequest(action,chat,detail,actionPrincipal);
        if(!quantityResponse.getIsOver()) return null;
        ContentResponse responseClone = ContentResponse.cloneContentResponse(ActionsOracleServices.quantityMax);
        responseClone.setActionMessage(String.format(ActionsOracleServices.quantityMax.getActionMessage(),quantityResponse.getDateOptionTrain()));
        return responseClone;
    }

    public String assignPrincipalData(Action action, Chat chat) {
        String messageOk = helperService.isPrincipal(chat.getEmpNdFil())
                ? action.getActionRespOkMessagePrincipal()
                : action.getActionRespOkMessage();


        if(action.getActionId()==524){
            if( histConstantServices.findIfHaveConstant(chat.getEmpNdFil()) ){
                System.out.println("entra aca");
                action.setActionRespOkMessage(action.getActionRespOkMessagePrincipal());
                String val = principalDataServices.getForSiglaAndEmpNd("urlSiteJob", chat.getEmpNd());
                return  String.format(action.getActionRespOkMessagePrincipal() ,  val);
            }
            else{
                String responsible;
                responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
                if(responsible != null)
                    return  String.format(messageOk ,  List.of(responsible).toArray());

            }

        }
        if(action.getActionId()==529) return messageOk;



        String[] siglas =
                helperService.isPrincipal(chat.getEmpNdFil())
                    ? action.getActionSiglaPrincipal().split("-")
                        :action.getActionSigla().split("-");

        List<String> values = new ArrayList<>();
        for (String sigla : siglas) {
            String val = principalDataServices.getForSiglaAndEmpNd(sigla, chat.getEmpNd());
            values.add(val == null ? "sin asignar" : val);
        }
        return  String.format(messageOk ,  values.toArray());
    }

    public static ContentResponse responseWithOptionsParam(ContentResponse response, Action action){
        ContentResponse responseClone = ContentResponse.cloneContentResponse(response);
        if(action == null || action.getActionOptionError() == null) {
            responseClone.setOptions(OptionsManageService.optionsPrincipal);
            return responseClone;
        }
        List<Option> options =  OptionsManageService.getOptionsByActionWithName(action.getActionOptionError());
        responseClone.setOptions(options);
        return responseClone;
    }

    private ContentResponse getContentResponse(Action action, Chat chat, List<String> dates, Integer actionIdReturn) {
        if(dates == null || dates.isEmpty())
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
        List<Option> options = new ArrayList<>(List.of());

        for (String label : dates) {
            options.add(
                    new Option(
                            action.getActionRespOkAction(),
                            label,
                            label,
                            label
                    )
            );
        }
        Action actionBack = actionServices.getActionForId(actionIdReturn);
        options.add(
                new Option(
                        actionBack.getActionRespOkAction(),
                        actionBack.getActionMessage(),
                        null,
                        null
                )
        );

        return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
    }


}
