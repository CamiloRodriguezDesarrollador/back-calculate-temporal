package com.microcode.client.service.oracle;

import com.microcode.client.clients.MailServices;
import com.microcode.client.entity.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.Company;
import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.jasper.JasperService;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.QuantityChatServices;
import com.microcode.client.service.mysql.Salt;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public static List<Action> actionsPrincipal;
    public static List<Option> optionsBasic;
    public static List<Option> optionsEps;
    public static List<Option> optionsBienestar;
    public static List<Option> Bienestar;
    public static List<Option> optionsPrincipal;
    public static List<Option> optionsDocument;
    public static List<Option> optionsEntities;
    public static List<Option> optionEndChat;
    public static List<Option> optionsLiq;
    public static List<Option> optionsPension;
    public static List<Option> optionsInca;
    public static List<Option> optionsCCF;

    public static ContentResponse unauthorized;
    public static ContentResponse notFound;
    public static ContentResponse quantityMax;
    public static ContentResponse noAction;
    public static ContentResponse timeOut;
    public static ContentResponse error;
    public static ContentResponse maxAttempts;


    //    Inicializar opciones

    @PostConstruct
    public void init() {
        actionServices.updateTypesChat();
    }

    //    Respuestas llamadas desde actions

    public final String MAIL_TEST = "yriascos@activos.com.co";
//    public final String MAIL_TEST = "javiercamilo75@gmail.com";

    public Chat initialChatIfNull(String chatId){
        chatSessionManager.updateChatActivity(chatId,null);
        return chatSessionManager.getChatById(chatId);
    }

    public ContentResponse getDataUser(Map<String,String> inputs, Action action) {
        try{
            String typeDocument = inputs.get("typeDocument");
            String document = inputs.get("document");
            String chatId = inputs.get("chatId");
            Chat chat;

            chat = chatSessionManager.getChatById(chatId);
            if(chat == null) chat = initialChatIfNull(chatId);

            if (typeDocument == null || document == null) return notFound;

            chat.setChatStart(new Date());

            Long docSearch = Long.valueOf(document);
            Employee employee = employeeService.findByIds(docSearch,typeDocument);
            if (employee == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), typeDocument, document),null, action);
            List<Contract> contracts = contractServices.findByIds(employee.getEplNd(), employee.getTdcTd());

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), typeDocument, document),null, action);


            if(employee.getEmail() == null) return ContentResponse.buildNotMail(null);
            if(employee.getEmpLastName() == null && employee.getEmplName() == null )
                return ContentResponse.buildNotMail(null);

            Contract contract = contractServices.findContractActive(employee.getEplNd() ,employee.getTdcTd());

            String firstName = employee.getEmplName() != null ? employee.getEmplName() : "";
            String lastName = employee.getEmpLastName() != null ? employee.getEmpLastName() : "";
            chat.setNames(firstName + " " + lastName);
            chat.setDocument(document);
            chat.setTypeDocument(typeDocument);
            chat.setChatMail(employee.getEmail());
            chat.setChatAuthenticated(false);
            if (contract != null) {
                chat.setEmpNd(contract.getEmpNd());
                chat.setTdcTd(contract.getTdcTd());
                chat.setEmpNdFil(contract.getEmpNdFil());
                chat.setTdcTdFil(contract.getTdcTdFil());
                chat.setCtoNumber(contract.getCtoNumero());
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
                mailServices.sendMailVerified(MAIL_TEST,code);
//                mailServices.sendMailVerified(chat.getChatMail(),code);
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
                chat.setChatAuthenticated(true);
                chat.setChatDateAuthorized(new Date());
                chatSessionManager.setChatById( chatId, chat );
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),optionsPrincipal, action);

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

    public ContentResponse getOptions(Map<String,String> inputs, Action action) {
        String chatId = inputs.get("chatId");
        Chat chat = chatSessionManager.getChatById(chatId);
        ContentResponse resp = this.validateInitial(chat);
        if(resp != null) return resp;

        try{
            String option = action.getActionTypeCall();

            List<Option> options = switch (option) {
                case "principal" -> optionsPrincipal;
                case "documents" -> optionsDocument;
                case "eps" -> optionsEps;
                case "bienestar" -> optionsBienestar;
                case "entities" -> optionsEntities;
                case "pension" -> optionsPension;
                case "incapacidades" -> optionsInca;
                case "ccf" -> optionsCCF;
                default -> List.of();
            };
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(),chat.getNames()),options, action);
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

            List<Contract> contracts = contractServices.findByIds(Long.valueOf(chat.getDocument()), chat.getTypeDocument());

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),optionsDocument, action);

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

            Action actionBack = getActionForId(1004);
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

    public ContentResponse getCertifiedJobDetail(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getCertifiedPay(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            Contract contract = contractServices.findContractActive(Long.valueOf(chat.getDocument()), chat.getTypeDocument());

            if(contract == null )
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),optionsDocument, action);

            chat.setCtoNumber(contract.getCtoNumero());

            List<Option> options = new ArrayList<>(List.of());
            List<String> labels = helperService.getPeriodLabels(contract.getPerSigla());

            for (String label : labels) {
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                label,
                                label,
                                label
                        )
                );
            }

            Action actionBack = getActionForId(1004);
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

    public ContentResponse getCertifiedPayDetail(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getCertifiedDian(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getInformationEps(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsEps);
    }

    public ContentResponse getDataIncludeEps(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getTransferEps(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getInformationBienestar(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsBienestar);
    }

    public ContentResponse getDataPsychology(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getDataConCesan(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getInformationPension(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsPension);
    }

    public ContentResponse getDataCesanAffiliation(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getIncAffiliation(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getInformationInca(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsInca);
    }

    public ContentResponse getInformationCcf(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsCCF);
    }

    public ContentResponse getDataInca(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse getInfRetiredCesa(Map<String,String> inputs, Action action) {
        return methodStandard(inputs,action,optionsBasic);
    }

    public ContentResponse getDocumentCesa(Map<String,String> inputs, Action action) {
        return methodStandard(inputs,action,optionsBasic);
    }

    public ContentResponse getInformationCCF(Map<String,String> inputs, Action action) {
        return methodStandardRedirect(inputs,action,optionsCCF);
    }

    public ContentResponse getDataRequirementsCcf(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse requirementCcfPersonalizate(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse requirementEpsPersonalizate(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

    public ContentResponse requirementAfpPersonalizate(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
    }

//   Metodo estandar

    private ContentResponse methodStandardRedirect(Map<String,String> inputs, Action action, List<Option> options) {
        try{
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    private ContentResponse methodStandard(Map<String,String> inputs, Action action, List<Option> options) {
        try {
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);

            String messageOk = action.getActionRespOkMessage();

            ContentResponse validateQuantity = validateQuantityOver(action, chat, detail);
            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            ContentResponse resp = this.validateInitial(chat);
            if (resp != null) return resp;

           Object validationAdditional = methodStandardAdditional(detail, action,chat);

           if(validationAdditional != null){
               if(validationAdditional instanceof String){
                   messageOk = (String) validationAdditional;
               }
               if(validationAdditional instanceof ContentResponse){
                   return (ContentResponse) validationAdditional;
               }
           }

            quantityChatServices.createForAction(
                    Integer.valueOf(action.getActionId().toString()),
                    chat.getTypeDocument(),
                    chat.getDocument(),
                    detail
            );

            return ContentResponse.buildContentResponseOk(
                    messageOk,
                    options,
                    action
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    public Object methodStandardAdditional(String detail, Action action, Chat chat) throws IOException {
        try{
            switch (action.getActionId()) {
                case 502:
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                    } else {
                        System.out.println(detail);
                        String[] part = detail.split("-");
                        Long contract = Long.parseLong(part[0]);
                        Long empNd = Long.parseLong(part[1]);
                        String tdcTd = part[2];

                        Contract cont = contractServices.findForCtoNumber(contract, empNd, tdcTd);

                        byte[] file = jasperService.getCertificateJob(
                                cont.getEmpNd(),
                                cont.getTdcTd(),
                                contract
                        );

                        System.out.println(file);
                        if (file == null) return error;
                        file = jasperService.protectPdfWithPassword(file, chat.getDocument());
                        mailServices.sendMailCertificates(
                                chat.getNames(), "Laboral", MAIL_TEST, file, "CertificadoLaboral.pdf"
                        ).subscribe();
                    }
                    return null;
                case 528:
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                    } else {
                        byte[] filePay = jasperService.getCertificatePay(
                                chat.getEmpNd(),
                                chat.getTdcTd(),
                                chat.getCtoNumber(),
                                detail
                        );
                        if (filePay == null)
                            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), optionsDocument, action);
                        filePay = jasperService.protectPdfWithPassword(filePay, chat.getDocument());

                        mailServices.sendMailCertificates(
                                chat.getNames(), "de pago", MAIL_TEST, filePay, "CertificacionPago.pdf"
                        ).subscribe();
                    }

                    return null;
                case 505:
                    System.out.println(chat.getEmpNdFil());
                    if (helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkMessage("<p>Es un trabajador de planta,por favor intenta otra opción 👇.</p>");
                    }else{
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
                        return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), optionsDocument, action);
                    action.setActionRespOkFile(url);
                     }
                    return null;
                case 529:
                    Company comp = entitiesServices.findForDataEpl(
                            chat.getEmpNd(), chat.getTdcTd(), chat.getCtoNumber(), "CCF"
                    );

                    if(comp == null || chat.getCtoNumber() == null) return ContentResponse.buildContentResponseFail(
                            String.format(action.getActionRespFailMessage()),
                            optionsCCF,
                            action
                    );
                    String nameCompany = comp.getEmpNombre();
                    String fileRequirements = "https://storage.googleapis.com/bucket_apps_public/CCF/requisitos.pdf";
                    String fileDeclaration = "https://storage.googleapis.com/bucket_apps_public/CCF/Declaraciones/"+comp.getEmpNd()+ ".pdf";
                    String fileVideo = "https://storage.googleapis.com/bucket_apps_public/CCF/instructivo.mp4";
                    mailServices.sendMailInformationCCF(
                            chat.getNames(),MAIL_TEST,nameCompany,fileRequirements,fileDeclaration,fileVideo
                    ).subscribe();

                    String mailSend = helperService.getEmailByNit(comp.getEmpNd().toString());
                    return String.format(action.getActionRespOkMessage(),nameCompany,mailSend);

                case 524 :
                    if(chat.getEmpNdFil() == null)
                        return ContentResponse.buildContentResponseFail(
                                String.format(action.getActionRespFailMessage()),
                                optionsInca,
                                action
                        );
                    String responsible = null;
                    if(helperService.isPrincipal(chat.getEmpNdFil())) {
                        action.setActionRespOkFile(null);
                        action.setActionRespOkMessage("<p>Genial !, los trabajadores internos deberán acceder al sitio del trabajador, por favor confirmame si tienes otro requerimiento 👇.</p>");
                    }else{
                        responsible = responsibleServices.findByCompany(chat.getTdcTdFil(), chat.getEmpNdFil());
                        if(responsible == null) responsible = "auxincapacidades3@activos.com.co";
                    }
                    return String.format(action.getActionRespOkMessage(),responsible);

            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse finalizedChat(Map<String,String> inputs, Action action) {
        try{
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),null, action);
        } catch (Exception e) {
            return error;
        }
    }

    public ContentResponse closeChat(Map<String,String> inputs, Action action) {
        try{
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),null, action);
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

    public ContentResponse moreOptions(Map<String,String> inputs, Action action) {
        try{
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),optionsPrincipal, action);
        } catch (Exception e) {
            return error;
        }
    }

//    Validación Inicial

    public ContentResponse validateInitial(Chat chat){
        if(chat == null || !chat.getChatAuthenticated() ) return unauthorized;
        if(chatSessionManager.validateTime(chat) || !chat.getChatAuthenticated()) return timeOut;
        chat.setChatDateAuthorized(new Date());
        return null;
    }

    public ContentResponse validateQuantityOver(Action action, Chat chat, String detail){
        QuantityResponse quantityResponse = chatSessionManager.validityQuantityRequest(action,chat,detail);
        if(!quantityResponse.getIsOver()) return null;
        ContentResponse responseClone = ContentResponse.cloneContentResponse(quantityMax);
        responseClone.setActionMessage(String.format(quantityMax.getActionMessage(),quantityResponse.getDateOptionTrain()));
        return responseClone;
    }

    // Actualización opciones

    public static void updateActions(List<Action> actions) {
        actionsPrincipal = actions;
    }
    public static void updateOptionsBasic(List<Option> options) {
        optionsBasic = options;
    }
    public static void updateOptionsDocument(List<Option> options) {
        optionsDocument = options;
    }
    public static void updateOptionsEps(List<Option> options) {
        optionsEps = options;
    }
    public static void updateOptionsBienestar(List<Option> options) {
        optionsBienestar = options;
    }
    public static void updateOptionsPension(List<Option> options) {
        optionsPension = options;
    }
    public static void updateOptionsInca(List<Option> options) {
        optionsInca = options;
    }
    public static void updateOptionsCcf(List<Option> options) {
        optionsCCF = options;
    }
    public static void updateOptionEntities(List<Option> options) {
        optionsEntities = options;
    }
    public static void updateOptionsLiq(List<Option> options) {
        optionsLiq = options;
    }
    public static void updateOptionEndChat(List<Option> options) {
        optionEndChat = options;
    }
    public static void updateOptionsPrincipal(List<Option> options) {
        optionsPrincipal = options;
    }


    // Helps
    public Action getActionForId(Integer actionId ){
        return actionsPrincipal.stream()
                .filter(e -> Objects.equals(e.getActionId(), actionId))
                .findFirst()
                .orElse(null);

    }

    public static ContentResponse wrapMessage(ContentResponse contentResponse){
        contentResponse.setActionMessage(Salt.wrapMessage(contentResponse.getActionMessage()));
        return contentResponse;
    }

    public static ContentResponse responseWithOptionsParam(ContentResponse response, Action action){
        ContentResponse responseClone = ContentResponse.cloneContentResponse(response);
        if(action == null || action.getActionTypeCall() == null) {
            responseClone.setOptions(optionsPrincipal);
            return responseClone;
        }
        List<Option> options = switch (action.getActionTypeCall()) {
            case "documents" -> optionsDocument;
            case "entities" -> optionsEntities;
            case "bienestar" -> optionsBienestar;
            case "eps" -> optionsEps;
            case "pension" -> optionsPension;
            case "incapacidades" -> optionsInca;
            case "ccf" -> optionsCCF;
            default -> optionsPrincipal;
        };

        responseClone.setOptions(options);
        return responseClone;
    }



}
