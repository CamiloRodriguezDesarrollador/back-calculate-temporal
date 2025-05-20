package com.microcode.client.service.oracle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcode.client.clients.MailServices;
import com.microcode.client.entity.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.Company;
import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.service.chat.ConsumeChatService;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.jasper.JasperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.QuantityChatServices;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    @PostConstruct
    public void init() {
        actionServices.updateTypesChat();
    }

    public final String MAIL_TEST = "yriascos@activos.com.co";
//    public final String MAIL_TEST = "cgonzalez@activos.com.co";

    public Chat initialChatIfNull(String chatId){
        chatSessionManager.updateChatActivity(chatId,null);
        return chatSessionManager.getChatById(chatId);
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
            }

            chatSessionManager.setChatById( chatId, chat );

            String mailUser = helperService.generateMail(employee.getEmail().toLowerCase());
            mailServices.ping();
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
                mailServices.sendMailVerified(MAIL_TEST,code,chat.getPrincipalRequest());
//                mailServices.sendMailVerified(chat.getChatMail(),code,chat.getPrincipalRequest());

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

            Contract contPay = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());

            if(contPay.getCtoNumero() == null) return withoutContract;

            Integer quantityPeriods = contPay.getPerSigla().equals("M") ? 3 : 6;
            List<String> dates = histNovServices.findPeriodsPay(
                    contPay.getCtoNumero(),contPay.getEmpNd(),contPay.getTdcTd(),quantityPeriods
            );

            if(dates == null || dates.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),OptionsManageService.optionsDocument, action);
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

//   Metodo estandar

    public ContentResponse methodStandardRedirect(Map<String,String> inputs, Action action) {
        try{
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);

            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            return ContentResponse.buildContentResponseOk(
                    String.format(action.getActionRespOkMessage(), chat.getNames()),
                    OptionsManageService.getOptionsByActionWithName(action.getActionOption()),
                    action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    public ContentResponse methodStandard(Map<String,String> inputs, Action actionOriginal) {
        try {
            Action action = actionOriginal.clone();
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);

            String messageOk = action.getActionRespOkMessage();

            ContentResponse validateQuantity = validateQuantityOver(action, chat, detail);
            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            ContentResponse resp = this.validateInitial(chat);
            if (resp != null) return resp;

            if(!actionServices.verifiedRequirementContractActive(chat,action)) return withoutContract;

            Object validationAdditional = methodStandardAdditional(detail, action,chat);

            if(validationAdditional != null){
                if(validationAdditional instanceof String){

                    messageOk = (String) validationAdditional;
                }
                if(validationAdditional instanceof ContentResponse){

                    return (ContentResponse) validationAdditional;
                }
            }

            quantityChatServices.createQuantityForAction(action,chat,detail);

            return ContentResponse.buildContentResponseOk(
                    messageOk,
                    OptionsManageService.getOptionsByActionWithName(action.getActionOption()),
                    action
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    public Object methodStandardAdditional(String detail, Action action, Chat chat) {
        try{
            switch (action.getActionId()) {
                case 502:
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                        return null;
                    } else {
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
                        mailServices.sendMailCertificates(
                                chat.getNames(), "Laboral", MAIL_TEST, file, "CertificadoLaboral.pdf",chat.getPrincipalRequest()
                        ).subscribe();
                    }
                    return null;

                case 528:
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                        return null;
                    } else {

                        Contract contPay = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());

                        byte[] filePay = jasperService.getCertificatePay(
                                contPay.getEmpNd(),
                                contPay.getTdcTd(),
                                contPay.getCtoNumero(),
                                detail
                        );
                        if (filePay == null)
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
                        filePay = jasperService.protectPdfWithPassword(filePay, chat.getDocument());

                        mailServices.sendMailCertificates(
                                chat.getNames(), "de pago", MAIL_TEST, filePay, "CertificacionPago.pdf",chat.getPrincipalRequest()
                        ).subscribe();
                    }
                    return null;

                case 505:
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                        return null;
                    }else{
                        Contract contLast = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());
                        if(contLast == null) return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
                        String url = certificatesService.getDataCertificatedDian(
                                contLast.getTdcTd(),
                                contLast.getEmpNd(),
                                chat.getTypeDocument(),
                                Long.valueOf(chat.getDocument()),
                                helperService.getDateCertifiedDianStartDate(),
                                helperService.getDateCertifiedDianEndDate()
                        );
                        if (url == null)
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), OptionsManageService.optionsDocument, action);
                        action.setActionRespOkFile(url);
                     }
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

                    //TODO: Estructura con mas de una URL
                    String fileRequirements = "https://storage.googleapis.com/bucket_apps_public/CCF/requisitos.pdf";
                    String fileDeclaration = "https://storage.googleapis.com/bucket_apps_public/CCF/Declaraciones/"+comp.getEmpNd()+ ".pdf";
                    String fileVideo = "https://storage.googleapis.com/bucket_apps_public/CCF/instructivo.mp4";
                    mailServices.sendMailInformationCCF(
                            chat.getNames(),MAIL_TEST,nameCompany,fileRequirements,fileDeclaration,fileVideo,chat.getPrincipalRequest()
                    ).subscribe();

                    String mailSend = helperService.getEmailByNit(comp.getEmpNd().toString());
                    return String.format(action.getActionRespOkMessage(),nameCompany,mailSend);

                case 524 :
                    if(helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkFile(null);
                        String urlSite = helperService.getUrlForPrincipal(chat.getEmpNd());
                        String message = "<p>Genial!, los trabajadores internos deberán acceder al <a href='%s' target='_blank'> sitio del trabajador<a>, por favor confirmame si tienes otro requerimiento 👇.</p>";
                        return String.format(message,urlSite);
                    }else{
                        String responsible;
                        responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
                        String mailAttInca = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"INC");
                        if(responsible == null) responsible = mailAttInca;
                        return String.format(action.getActionRespOkMessage(),responsible);
                    }

                case 533 :
                case 507 :
                case 521 :
                case 522 :
                    Contract cont = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());
                    String mailAttAfp = helperService.getEmailEpsPrincipal(cont.getEmpNd(),"AFP");
                    return String.format(action.getActionRespOkMessage(),mailAttAfp);
                case 532 :
                case 515 :
                    String mailAttEps = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"EPS");
                    return String.format(action.getActionRespOkMessage(),mailAttEps);
                case 530 :
                    String mailAttCcf = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"CCF");
                    return String.format(action.getActionRespOkMessage(),mailAttCcf);
                case 506 :
                    String mailAttR = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"RRHH");
                    return String.format(action.getActionRespOkMessage(),mailAttR);
                case 516 :
                    String mailEpsP = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"PPAL");
                    String mailEpsTras = helperService.getEmailEpsPrincipal(chat.getEmpNd(),"EPS");
                    return String.format(action.getActionRespOkMessage(),mailEpsP,mailEpsTras,mailEpsTras);
                case 101 :
                    String statusLiq = certificatesService.getStatusLiq(
                            chat.getTypeDocument(),
                            Long.valueOf(chat.getDocument())
                    );
                    System.out.println(statusLiq);
                    if(statusLiq == null) return error;
                    return String.format(action.getActionRespOkMessage(),statusLiq.substring(0, 1).toUpperCase() + statusLiq.substring(1).toLowerCase());


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

    public ContentResponse validateQuantityOver(Action action, Chat chat, String detail){
        if(action.getActionQuantity() == null || action.getActionDaysQuantity() == null) return null;
        QuantityResponse quantityResponse = chatSessionManager.validityQuantityRequest(action,chat,detail);
        if(!quantityResponse.getIsOver()) return null;
        ContentResponse responseClone = ContentResponse.cloneContentResponse(ActionsOracleServices.quantityMax);
        responseClone.setActionMessage(String.format(ActionsOracleServices.quantityMax.getActionMessage(),quantityResponse.getDateOptionTrain()));
        return responseClone;
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




}
