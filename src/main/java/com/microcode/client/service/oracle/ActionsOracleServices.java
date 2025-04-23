package com.microcode.client.service.oracle;

import com.microcode.client.clients.MailServices;
import com.microcode.client.entity.QuantityResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.oracle.Contract;
import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.QuantityChatServices;
import com.microcode.client.service.mysql.Salt;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@AllArgsConstructor
@Setter
@Getter
public class ActionsOracleServices {

    private final EmployeeServices employeeService;
    private final ContractServices contractServices;
    private final ChatSessionManager chatSessionManager;
    private final QuantityChatServices quantityChatServices;
    private final MailServices mailServices;
    private final ActionServices actionServices;
    public static List<Option> optionsBasic;
    public static List<Option> optionsEps;
    public static List<Option> optionsBienestar;
    public static List<Option> Bienestar;
    public static List<Option> optionsPrincipal;
    public static List<Option> optionsDocument;
    public static List<Option> optionsEntities;
    public static List<Option> optionEndChat;
    public static List<Option> optionsLiq;
    public static List<Action> actionsPrincipal;

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

    public ContentResponse getDataUser(Map<String,String> inputs, Action action) {
        try{
            String typeDocument = inputs.get("typeDocument");
            String document = inputs.get("document");
            String chatId = inputs.get("chatId");
            Chat chat;

            chat = chatSessionManager.getChatById(chatId);
            if(chat == null){
                chatSessionManager.updateChatActivity(chatId,null);
                chat = chatSessionManager.getChatById(chatId);
            }

            if (typeDocument == null || document == null) return notFound;

            chat.setChatStart(new Date());

            Long docSearch = Long.valueOf(document);
            Employee employee = employeeService.findByIds(docSearch,typeDocument);
            if (employee == null)
                return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage(), typeDocument, document),null, action);

            chat.setNames(employee.getEmplName() + " " + employee.getEmpLastName() );
            chat.setDocument(document);
            chat.setTypeDocument(typeDocument);
            chat.setChatMail(employee.getEmail());
            chat.setChatAuthenticated(false);
            chatSessionManager.setChatById( chatId, chat );

            String mailUser = generateMail(employee.getEmail().toLowerCase());
            mailServices.ping();
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), mailUser),null, action);
        } catch (Exception e) {
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
                String code = generateCode();
                chat.setChatCode(code);
                chat.setChatAttempts(1);
                chat.setChatDateCode(new Date());
                mailServices.sendMailVerified("javiercamilo75@gmail.com",code);
//                mailServices.sendMailVerified(chat.getChatMail(),code);
                return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage()), null, action);
            }

            return ContentResponse.buildContentResponseFail(String.format(action.getActionRespFailMessage()), null, action);
        } catch (Exception e) {
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
                default -> List.of();
            };
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(),chat.getNames()),options, action);
        } catch (Exception e) {

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
                var contrato = contracts.get(i);
                options.add(
                        new Option(
                                action.getActionRespOkAction(),
                                labels.get(i),
                                contrato.getCtoNumero().toString(),
                                contrato.getCtoNumero().toString()
                        )
                );
            }

            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
        } catch (Exception e) {
            return error;
        }

    }

    public ContentResponse getCertifiedJobDetail(Map<String,String> inputs, Action action) {
        return methodStandard(inputs, action, optionsBasic);
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


//   Metodo estandar

    private ContentResponse methodStandardRedirect(Map<String,String> inputs, Action action, List<Option> options) {
        try{
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);
            ContentResponse resp = this.validateInitial(chat);
            if(resp != null) return resp;
            return ContentResponse.buildContentResponseOk(String.format(action.getActionRespOkMessage(), chat.getNames()),options, action);
        } catch (Exception e) {
            return error;
        }
    }


    private ContentResponse methodStandard(Map<String,String> inputs, Action action, List<Option> options) {
        try {
            String detail = inputs.get("detail");
            String chatId = inputs.get("chatId");
            Chat chat = chatSessionManager.getChatById(chatId);

            ContentResponse validateQuantity = validateQuantityOver(action, chat, detail);
            if (validateQuantity != null) return responseWithOptionsParam(validateQuantity, action);

            ContentResponse resp = this.validateInitial(chat);
            if (resp != null) return resp;

            quantityChatServices.createForAction(
                    Integer.valueOf(action.getActionId().toString()),
                    chat.getTypeDocument(),
                    chat.getDocument(),
                    detail
            );

            return ContentResponse.buildContentResponseOk(
                    String.format(action.getActionRespOkMessage()),
                    options,
                    action
            );
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

    public String generateCode(){
//        String CARACTERES = "abcdefghijklmnopqrstuvwxyz0123456789";
        String CARACTERES = "0123456789";
        SecureRandom random = new SecureRandom();

        return random.ints(6, 0, CARACTERES.length())
                .mapToObj(i -> String.valueOf(CARACTERES.charAt(i)))
                .reduce((a, b) -> a + b)
                .orElse("");
    }

    public String generateMail(String email){

        int atIndex = email.indexOf('@');
        String visiblePart = email.substring(0, Math.min(3, atIndex));
        String maskedPart = "*".repeat(Math.max(0, atIndex - visiblePart.length()));
        return visiblePart + maskedPart + email.substring(atIndex);
    }

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
        List<Option> options;

        if(action.getActionTypeCall().equals("documents")) options = optionsDocument;
        else if(action.getActionTypeCall().equals("entities")) options = optionsEntities;
        else if(action.getActionTypeCall().equals("bienestar")) options = optionsBienestar;
        else if(action.getActionTypeCall().equals("eps")) options = optionsEps;
        else options = optionsPrincipal;

        responseClone.setOptions(options);
        return responseClone;
    }



}
