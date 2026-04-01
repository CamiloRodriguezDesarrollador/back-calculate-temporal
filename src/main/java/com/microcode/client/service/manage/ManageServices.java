package com.microcode.client.service.manage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcode.client.clients.ConnectExternalServices;
import com.microcode.client.clients.MailServices;
import com.microcode.client.clients.NotifyServices;
import com.microcode.client.entity.general.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.*;
import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.general.Option;
import com.microcode.client.service.chat.ChatSessionManagerI;
import com.microcode.client.service.chat.ConsumeChatServiceI;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.jasper.JasperServiceI;
import com.microcode.client.service.mysql.*;
import com.microcode.client.service.oracle.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class ManageServices {

    public static ContentResponse unauthorized;
    public static ContentResponse notFound;
    public static ContentResponse quantityMax;
    public static ContentResponse noAction;
    public static ContentResponse timeOut;
    public static ContentResponse error;
    public static ContentResponse withoutContract;
    public static ContentResponse maxAttempts;
    public static ContentResponse otherSessionActive;

    private final EmployeeServicesI employeeService;
    private final ResponsibleServicesI responsibleServices;
    private final ContractServicesI contractServices;
    private final ChatSessionManagerI chatSessionManager;
    private final QuantityChatServicesI quantityChatServices;
    private final JasperServiceI jasperService;
    private final EntitiesServicesI entitiesServices;
    private final ActionServicesI actionServices;
    private final HistNovServicesI histNovServices;
    private final ConsumeChatServiceI consumeChatService;
    private final ContributionNovServicesI contributionNovServices;
    private final PrincipalDataServicesI principalDataServices;
    private final HistConstantServicesI histConstantServices;
    private final IncapacityServicesI incapacityServices;
    private final LibIngServicesI libIngServices;
    private final QuestionServicesI questionServices;
    private final StatusChatServicesI statusChatServices;
    private final EmployeePhoneServicesI employeePhoneServices;
    private final CertificatesServiceI certificatesService;
    private final ManageAdditionalServicesI manageAdditionalServices;
    private final LoanServicesI loanServicesI;

    private final MailServices mailServices;
    private final ConnectExternalServices connectExternalServices;
    private final NotifyServices notifyServices;

    private final OptionsManageService optionsManageService;
    private final HelperService helperService;

    @PostConstruct
    public void init() {
        actionServices.updateTypesChat();
        principalDataServices.updateDataPrincipal();
        questionServices.updateQuestionCalification();
    }

    public Chat initialChatIfNull(String chatId, String companyId){
        chatSessionManager.updateChatActivity(chatId,companyId,null);
        return chatSessionManager.getChatById(chatId,companyId);
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
            String companyId = inputs.get("companyId");

            if (document != null) document = document.replaceAll("[^a-zA-Z0-9]", "");
            if (typeDocument != null) typeDocument = typeDocument.replaceAll("[^a-zA-Z0-9]", "");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> idsPrincipal = objectMapper.readValue(principalRequest, new TypeReference<>() {});

            Chat chat;

            chat = chatSessionManager.getChatById(chatId,companyId);
            if(chat == null) chat = initialChatIfNull(chatId, companyId);
            chat.setCompanyId(companyId);
            chat.setTypeChat(2);

            chat.setChatStart(new Date());
            if(name != null && !name.equals("null")) chat.setNames(name);
            if(document != null && !document.equals("null")) chat.setDocument(document);
            if(typeDocument != null && !typeDocument.equals("null")) chat.setTypeDocument(typeDocument);
            if(email != null && !email.equals("null") ) chat.setChatMail(email);
            if(phone != null && !phone.equals("null") ) chat.setChatPhone(phone);

            chat.setChatAuthenticated(false);
            chat.setPrincipalRequest(idsPrincipal);
            chat.setEmpNd(idsPrincipal.get(0));
            chat.setTdcTd("NI");


            if(chat.getNames() != null && chat.getChatPhone() != null && chat.getChatMail() != null && chat.getDocument() != null && chat.getTypeDocument() != null ){
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()),OptionsManageService.optionsPrincipalExternal, action,null);
            }

            List<String> missing = new ArrayList<>();
            if (chat.getTypeDocument() == null || chat.getTypeDocument().isBlank()) missing.add("Tipo de documento (C.C., C.E., P.A. (o PAS), C.D., PT, NIT)");
            if (chat.getDocument() == null || chat.getDocument().isBlank())         missing.add("Numero de documento");
            if (chat.getNames() == null || chat.getNames().isBlank())                 missing.add("Nombres");
            if (chat.getChatPhone() == null || chat.getChatPhone().isBlank())               missing.add("Celular");
            if (chat.getChatMail() == null || chat.getChatMail().isBlank())               missing.add("Correo electrónico");

            boolean isFirstTime = missing.size() == 5;

            String message = isFirstTime
                    ? helperService.defineChatType(2)
                    : "Aún me hacen falta estos datos 📝:";

            chat.setChatAuthenticated(true);
            chat.setChatDateAuthorized(new Date());

            List<Option> optionsText = List.of(
                    new Option(200, "\n🔸 *" + String.join("*\n🔸 *", missing) + "*", "Te estan enviando el dato faltante", null)
            );

            Action act = action.clone();
            act.setActionRedirect(200);
            act.setActionRespOkRequest("select");

            return ContentResponse.buildContentResponseOk(message,optionsText, act,null);
        } catch (Exception e) {
            log.error("Error {} " , e.getMessage());
            return this.responseWithOptionsParam(error,action);
        }

    }

    public ContentResponse getDataUser(Map<String,String> inputs, Action action) {
        try{
            String typeDocument = inputs.get("typeDocument");
            String document = inputs.get("document");
            String chatId = inputs.get("chatId");
            String principalRequest = inputs.get("principalRequest");
            String companyId = inputs.get("companyId");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> idsPrincipal = objectMapper.readValue(principalRequest, new TypeReference<>() {});

            Chat chat;
            if (document != null) document = document.replaceAll("[^a-zA-Z0-9]", "");
            if (typeDocument != null) typeDocument = typeDocument.replaceAll("[^a-zA-Z0-9]", "");

            chat = chatSessionManager.getChatById(chatId,companyId);

            log.info("Chat: {}" ,chat);

            if(chat == null) chat = initialChatIfNull(chatId,companyId);
            if(document != null && !document.equals("null")) chat.setDocument(document);
            if(typeDocument != null && !typeDocument.equals("null")) chat.setTypeDocument(typeDocument);

            chat.setCompanyId(companyId);
            chat.setTypeChat(2);


            if (chat.getTypeDocument() == null || chat.getDocument()  == null)
            {
                List<String> missing = new ArrayList<>();
                if (chat.getTypeDocument() == null || chat.getTypeDocument().isBlank()) missing.add("Tipo de documento (C.C., C.E., P.A. (o PAS), C.D., PT, NIT)");
                if (chat.getDocument() == null || chat.getDocument().isBlank())         missing.add("Numero de documento");

                boolean isFirstTime = missing.size() == 2;

                String message = isFirstTime
                        ? helperService.defineChatType(1)
                        : "Aún me hacen falta estos datos 📝: ";

                List<Option> optionsText = List.of(
                        new Option(1, "\n🔸 *" + String.join("*\n🔸 *", missing) + "*", "Te estan enviando el dato faltante", null)
                );

                Action act = action.clone();
                act.setActionRedirect(1);
                act.setActionRespOkRequest("select");

                return ContentResponse.buildContentResponseOk(message,optionsText, act,null);
            }

            chat.setChatStart(new Date());

            Long docSearch = Long.valueOf(chat.getDocument());
            Employee employee = employeeService.findByIds(docSearch,chat.getTypeDocument());

            if (employee == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), chat.getTypeDocument(), chat.getDocument()),null, action,null);
            List<Contract> contracts = contractServices.findByIds(employee.getEplNd(), employee.getTdcTd(),idsPrincipal);

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), chat.getTypeDocument(), chat.getDocument()),null, action,null);


            if(employee.getEmail() == null) return ContentResponse.buildNotMail(null);
            if(employee.getEmpLastName() == null && employee.getEmplName() == null )
                return ContentResponse.buildNotMail(null);

            Contract contract = contractServices.findContractActive(employee.getEplNd() ,employee.getTdcTd(),idsPrincipal);

            String firstName = employee.getEmplName() != null ? employee.getEmplName() : "";
            String lastName = employee.getEmpLastName() != null ? employee.getEmpLastName() : "";
            chat.setNames(firstName + " " + lastName);

            chat.setChatMail(employee.getEmail());
            chat.setChatAuthenticated(false);
            chat.setPrincipalRequest(idsPrincipal);
            if (contract != null) {
                chat.setEmpNd(contract.getEmpNd());
                chat.setTdcTd(contract.getTdcTd());
                chat.setEmpNdFil(contract.getSucNombreFil().equals("PROYECTO ESPECIAL ALIADOS") ? 999999999 : contract.getEmpNdFil());
                chat.setTdcTdFil(contract.getTdcTdFil());
                chat.setCtoNumber(contract.getCtoNumero());
                chat.setPerSigla(contract.getPerSigla());
                chat.setContractActive(true);

            }else{
                Contract cont = contractServices.findContractForEpl(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());
                chat.setEmpNd(cont.getEmpNd());
                chat.setTdcTd(cont.getTdcTd());
                chat.setEmpNdFil(cont.getSucNombreFil().equals("PROYECTO ESPECIAL ALIADOS") ? 999999999 : cont.getEmpNdFil());
                chat.setTdcTdFil(cont.getTdcTdFil());
                chat.setCtoNumber(cont.getCtoNumero());
                chat.setPerSigla(cont.getPerSigla());
                chat.setContractActive(false);
            }

            chatSessionManager.setChatById( chatId , companyId, chat );

            String mailUser = helperService.generateMail(employee.getEmail().toLowerCase());
            mailServices.ping();
            connectExternalServices.ping();
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), mailUser),null, action,null);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;

        }
    }

    public ContentResponse verifiedMail(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");

            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            chat.setChatStart(new Date());

            String isMailCorrect = inputs.get("isMailCorrect");
            String detail = inputs.get("detail");

            String mailFlag = (isMailCorrect != null && !isMailCorrect.isBlank())
                    ? isMailCorrect
                    : detail;

            if (mailFlag.equals("Y")) {
//                String code = "123456";
                String code = helperService.generateCode();
                chat.setChatCode(code);
                chat.setChatAttempts(1);
                chat.setChatDateCode(new Date());

                String contentMail = String.format(action.getActionRepOkMail(),code);
                String subject = String.format(action.getActionRepOkMailSubject(),code);

//                mailServices.sendMailChat(MAIL_TEST,contentMail,subject,chat.getPrincipalRequest());
                mailServices.sendMailChat(chat.getChatMail(),contentMail,subject,chat.getPrincipalRequest());

                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()), null, action,null);
            }

            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), null, action,null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }
    }

    public ContentResponse verified(Map<String,String> inputs, Action action) {
        try{

            String code = inputs.get("codeVerification");
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");

            String codeVerified = (code != null && !code.isBlank())
                    ? code
                    : detail;

            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            if(chatSessionManager.validateTimeCode(chat)) return timeOut;

            if(codeVerified.toLowerCase().equals(chat.getChatCode()) ){

                try{
                    Chat chatLast = chatSessionManager.getAuthorizedChatByDocumentAndType(chat.getDocument(),chat.getTypeDocument(), companyId);
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
                chatSessionManager.setChatById( chatId, companyId, chat );
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),OptionsManageService.optionsPrincipal, action,null);

            }

            if(chatSessionManager.validateAttemptsCode(chat)) return maxAttempts;

            chat.setChatAttempts(chat.getChatAttempts()+1);
            int att = chat.getChatAttempts()-4;
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(),att*-1),null, action,null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse getCertifiedJob(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);

            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            List<Contract> contracts = contractServices.findByIds(Long.valueOf(chat.getDocument()), chat.getTypeDocument(), chat.getPrincipalRequest());

            if(contracts == null || contracts.isEmpty())
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),optionsManageService.getOptionsByActionWithOption(action.getActionOptionError()), action,null);

            List<Option> options = new ArrayList<>(List.of());
            List<String> labels = List.of("Último contrato", "Contrato Anterior");

            for (int i = 0; i < contracts.size() && i < labels.size(); i++) {
                var contract = contracts.get(i);
                String baseValue = contract.getCtoNumero()+"-"+contract.getEmpNd()+"-"+contract.getTdcTd();

                // Normal
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                "📄 " + labels.get(i),
                                baseValue + "-N",
                                baseValue + "-N"
                        )
                );

                // Con salario promedio (con negrilla en el texto)
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                "📊 " + labels.get(i) + " con <b style='font-size:12px'>Salario Promedio</b>",
                                baseValue + "-S",
                                baseValue + "-S"
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

            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action,null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return error;
        }

    }

    public ContentResponse getCertifiedPay(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);
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
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);
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
            String companyId = inputs.get("companyId");

            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;

            if(detail == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), chat.getNames()),
                        optionsManageService.getOptionsByActionWithOption(action.getActionOptionError())
                        , action,null);

            chat.setPeriodPlanilla(detail);

            List<Option> options = new ArrayList<>(List.of());

            for (String label : List.of("Sin IBC del periodo "+detail,"Con IBC del periodo "+detail)) {
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

            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action,null);
        } catch (Exception e) {
            return error;
        }

    }

    public ContentResponse inactiveChat(Map<String,String> inputs, Action action) {
        try{
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            chat.setChatAuthenticated(false);
            chatSessionManager.setChatById(chatId, companyId,chat);
            return timeOut;
        } catch (Exception e) {
            return error;
        }
    }

    public ContentResponse getInformationIncaStatus(Map<String,String> inputs, Action action) {
        try {
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;
            if(chat.getContractActive() == null ||  !chat.getContractActive()) return responseWithOptionsParam(withoutContract,action);

            if(helperService.isPrincipal(chat.getEmpNdFil())){

                ContentResponse validateQuantity = validateQuantityOver(action, chat, "0", chat.getEmpNd().toString());
                if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

                String messageOk = principalDataServices.getForSiglaAndEmpNd("plantaIncapacidad", 0L);
                String val = principalDataServices.getForSiglaAndEmpNd("urlSitioTrabajador", chat.getEmpNd());

                quantityChatServices.createQuantityForAction(action,chat,"0", chat.getEmpNd().toString());

                List<Option> options = new ArrayList<>(
                        optionsManageService.getOptionsByActionWithOption(action.getActionOption())
                );

                if (action.getActionRedirect() != null) {
                    options.add(0,
                            new Option(
                                    action.getActionRedirect(),
                                    "Regresar al menú anterior \uD83D\uDD19",
                                    null,
                                    null
                            )
                    );
                }

                return ContentResponse.buildContentResponseOk(
                        String.format(messageOk,val),
                        options,
                        action,
                        null
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
            String companyId = inputs.get("companyId");
            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            chat.setChatDateAuthorized(new Date());

            if(chat.getTypeChat() == 1){
                ContentResponse resp = this.validateInitial(chat);
                if(resp != null) return resp;
            }

            ContentResponse validateQuantity = validateQuantityOver(action, chat, "redirect",chat.getEmpNd().toString());

            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            if(!actionServices.verifiedRequirementContractActive(chat,action))
                return this.responseWithOptionsParam(withoutContract,action);


            quantityChatServices.createQuantityForAction(action,chat,"redirect",chat.getEmpNd().toString());

            return ContentResponse.buildContentResponseOk(
                    String.format(action.getActionRespOkMessage(), chat.getNames()),
                    optionsManageService.getOptionsByActionWithOption(action.getActionOption()),
                    action,
                    OptionsManageService.questionsCalification

            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this.responseWithOptionsParam(error,action);
        }
    }

    public ContentResponse methodStandard(Map<String,String> inputs, Action actionOriginal) {
        try {
            Action action = actionOriginal.clone();
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            String companyId = inputs.get("companyId");

            Chat chat = chatSessionManager.getChatById(chatId,companyId);
            chat.setChatDateAuthorized(new Date());

            if(!actionServices.verifiedRequirementContractActive(chat,action)) return responseWithOptionsParam(withoutContract,action);

            String messageOk = helperService.isPrincipal(chat.getEmpNdFil()) ? action.getActionRespOkMessagePrincipal() : action.getActionRespOkMessage();

            ContentResponse validateQuantity = validateQuantityOver(action, chat, detail,chat.getEmpNd().toString());

            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            if(chat.getTypeChat() == 1){
                ContentResponse resp = this.validateInitial(chat);
                if (resp != null) return resp;
            }

            if(action.getActionSigla() != null || action.getActionSiglaPrincipal() != null){
                messageOk = manageAdditionalServices.assignPrincipalData(action,chat);
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

            List<Option> options = new ArrayList<>(
                    optionsManageService.getOptionsByActionWithOption(action.getActionOption())
            );

            if (action.getActionRedirect() != null) {
                options.add(0,
                        new Option(
                                action.getActionRedirect(),
                                "Regresar al menú anterior \uD83D\uDD19",
                                null,
                                null
                        )
                );
            }

            return ContentResponse.buildContentResponseOk(
                    messageOk,
                    options,
                    action,
                    null
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this.responseWithOptionsParam(error,actionOriginal);
        }
    }

    public Object methodStandardAdditional(String detail, Action action, Chat chat) {
        try{
            return switch (action.getActionId()) {
                case 502 -> manageAdditionalServices.generateCertJob(detail, action, chat);
                case 528 -> manageAdditionalServices.generateCertPay(detail, action, chat);
                case 505 -> manageAdditionalServices.generateCertDian(detail, action, chat);
                case 529 -> manageAdditionalServices.generateDataCCF(detail, action, chat);
                case 101 -> manageAdditionalServices.generateStatusLiq(detail, action, chat);
                case 535 -> manageAdditionalServices.generatePlanillaIbc(detail, action, chat);
                case 537 -> manageAdditionalServices.generateIncapacities(detail, action, chat);
                case 105 -> manageAdditionalServices.generatePqr(detail, action, chat);
                case 551 -> manageAdditionalServices.generateCall(detail, action, chat);
                case 549 -> manageAdditionalServices.generateBienestar(detail, action, chat);
                case 2024 -> manageAdditionalServices.generateCarnet(detail, action, chat);
                case 2026 -> manageAdditionalServices.getDataFedac(detail, action, chat);
                case 2027 -> manageAdditionalServices.getDataFedacApproved(detail, action, chat);
                default -> null;
            };
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
        ContentResponse responseClone = ContentResponse.cloneContentResponse(ManageServices.quantityMax);
        responseClone.setActionMessage(String.format(ManageServices.quantityMax.getActionMessage(),quantityResponse.getDateOptionTrain()));
        return responseClone;
    }

    public ContentResponse responseWithOptionsParam(ContentResponse response, Action action){
        ContentResponse responseClone = ContentResponse.cloneContentResponse(response);
        if(action == null || action.getActionOptionError() == null) {
            responseClone.setOptions(OptionsManageService.optionsPrincipal);
            return responseClone;
        }
        List<Option> options =  optionsManageService.getOptionsByActionWithOption(action.getActionOptionError());
        responseClone.setOptions(options);
        return responseClone;
    }

    private ContentResponse getContentResponse(Action action, Chat chat, List<String> dates, Integer actionIdReturn) {
        List<Option> optionsError = optionsManageService.getOptionsByActionWithOption(action.getActionOptionError());
        if(dates == null || dates.isEmpty())
            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()),
                    optionsError, action,null);

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

        return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action,null);
    }


}
