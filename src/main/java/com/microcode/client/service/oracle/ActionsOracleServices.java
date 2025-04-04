package com.microcode.client.service.oracle;

import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.Option;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.service.mysql.QuantityChatServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActionsOracleServices {

    private final EmployeeServices employeeService;
    private final ContractServices contractServices;
    private final ChatSessionManager chatSessionManager;
    private final QuantityChatServices quantityChatServices;
    private final HttpServletRequest request;

    public ActionsOracleServices(EmployeeServices employeeService
            , ContractServices contractServices
            , ChatSessionManager chatSessionManager
            , QuantityChatServices quantityChatServices
            , HttpServletRequest request ) {
        this.employeeService = employeeService;
        this.contractServices = contractServices;
        this.chatSessionManager = chatSessionManager;
        this.quantityChatServices = quantityChatServices;
        this.request = request;
    }

    public List<Option> optionsPrincipal =
            List.of(
                    new Option(3,"Certificados laborales 📄",null),
                    new Option(4,"Comprobante de pago 💲",null),
                    new Option(5,"Planilla Autoliquidación 📆",null),
                    new Option(6,"Ingresos y retenciones 💰",null),
                    new Option(7,"Información CCF 🗃",null),
                    new Option(8,"Información EPS 🧂",null),
                    new Option(998,"Finalizar chat 🚪",null));

    public ContentResponse responseUnauthorized
            = new ContentResponse(
                    "<p>Para utilizar esta opción debes estar autenticado.<p>",
            null, "verified",null);

    public ContentResponse responseTimeOut
            = new ContentResponse(
            "<p>Se ha vencido el tiempo de autorización, por favor verificar nuevamente tu identificación.<p>",
            null, "verified",null);


    public ContentResponse initialChat(Map<String,String> inputs){
        return new ContentResponse(
                "<p>Chat Iniciado</p>",
                null, "select", 1
        );
    }

    public ContentResponse getDataUser(Map<String,String> inputs) {
        String typeDocument = inputs.get("typeDocument");
        String document = inputs.get("document");
        String chatId = inputs.get("chatId");
        String ip = inputs.get("ip");

        Chat chat = chatSessionManager.getChatById(chatId);
        chat.setChatIp(ip);

        if (typeDocument == null || document == null) {
            return new ContentResponse(
                    """
                    <p>No has ingresado correctamente los datos.
                    """,
                    null    , "verified", null
            );
        }
        Long docSearch = Long.valueOf(document);
        Employee employee = employeeService.findByIds(docSearch,typeDocument);
        if (employee == null) {
            return new ContentResponse(
                    String.format("""
                    <p>La persona a la cual solicitas información <strong>%s-%s</strong>, no existe en nuestro sistema.
                    Por favor intenta nuevamente, o valida con el ejecutivo de cuenta.
                    """, typeDocument, document),
                    null, "verified", null
            );
        }

        String code = generateCode();
        System.out.println("Code: " + code);
        chat.setChatCode(code);
        chat.setDocument(document);
        chat.setTypeDocument(typeDocument);
        chat.setChatMail(employee.getEmail());
        chatSessionManager.setChatById(chatId, chat );

        String mailUser = employee.getEmail().toLowerCase().replaceAll("(?<=.).(?=[^@]*?@)", "*");

        return new ContentResponse(
                String.format("""
                <p>Estás solicitando información para la persona con documento <strong>%s-%s</strong>.
                Para continuar, por favor verifica el código enviado a tu correo.
                Tu correo registrado es: <strong>%s</strong></p>
                """, typeDocument, document, mailUser),
                null, "number", 2
        );
    }

    public ContentResponse verified(Map<String,String> inputs) {
        String codeVerified = inputs.get("codeVerification");
        String chatId = inputs.get("chatId");
        String actionId = inputs.get("actionId");

        Chat chat = chatSessionManager.getChatById(chatId);

        if(codeVerified.toLowerCase().equals(chat.getChatCode())){
            chat.setChatAuthenticated(true);
            chat.setChatDateAuthorized(new Date());
            chatSessionManager.setChatById(chatId, chat );
            quantityChatServices.createForAction( Integer.valueOf(actionId), chat.getTypeDocument(), chat.getDocument()  );

            return new ContentResponse(
                    """
                        <p>Tu correo ha sido verificado con exito.
                        A continuación encontraras las opciones que puedes elegir.
                        Cuentas con <strong> 15 minutos </strong>, de lo contrario deberás volver a generar el código </p>
                        """,

                    optionsPrincipal, "select",997);
        }else {
            return new ContentResponse(
                    """
                    <p>El codigo informado no corresponde, por favor intenta nuevamente.</p>
                    """,
                    null, "number", 2
            );
        }

    }

    public ContentResponse getCertifiedJob(Map<String,String> inputs) {
        String chatId = inputs.get("chatId");
        Chat chat = chatSessionManager.getChatById(chatId);
        if(chat == null || !chat.getChatAuthenticated() ) return responseUnauthorized;
        if(chatSessionManager.validateTime(chat) || !chat.getChatAuthenticated()) return responseTimeOut;

        List<String> contracts = contractServices.findByIds(Long.valueOf(chat.getDocument()), chat.getTypeDocument());

        if(contracts == null || contracts.isEmpty()){
            return new ContentResponse("<p>El trabajador no cuenta con registro de contratos, por favor elige otra documento o otra opción.<p>",
                    null, "verified",null);
        }

        List<Option> options = new java.util.ArrayList<>(List.of());
        contracts.forEach(e->{
                    options.add(new Option(9,"Contrato: " + e,e  ));
                }
        );

        return new ContentResponse("<p>Estas solicitando un certificado laboral, por favor selecciona el contrato.<p>",
                options, "select",null);
    }

    public ContentResponse getCertifiedJobDetail(Map<String,String> inputs) {
        String chatId = inputs.get("chatId");
        String actionId = inputs.get("actionId");
        Chat chat = chatSessionManager.getChatById(chatId);
        if(chat == null || !chat.getChatAuthenticated() ) return responseUnauthorized;
        if(chatSessionManager.validateTime(chat) || !chat.getChatAuthenticated()) return responseTimeOut;

        String detail = inputs.get("detail");
        quantityChatServices.createForAction( Integer.valueOf(actionId), chat.getTypeDocument(), chat.getDocument()  );

        return new ContentResponse(
                String.format("""
                    <p>Se ha remitido el certificado del contrato <strong>%s</strong> a tu correo.
                    Por favor confirma si requieres algo mas</p>
                    """, detail),
                optionsPrincipal, "select",9);

    }

    public ContentResponse notFound() {
        return new ContentResponse(
                "<p>La opción selecciona no se encuentra habilitada, por favor selecciona una diferente<p>",
                optionsPrincipal, "select",null);
    }

    public ContentResponse quantityMax() {
        return new ContentResponse(
                "<p>Has superado el limite de solicitudes de este acción, por favor intenta el siguiente mes, o intenta una opción diferente.<p>",
                optionsPrincipal, "select",null);
    }

    public ContentResponse notAction() {
        return new ContentResponse(
                "<p>La opción solicitada no puede ser procesada.<p>",
                optionsPrincipal, "select",null);
    }

    public ContentResponse finalizedChat(Map<String,String> inputs) {
        return new ContentResponse(
                "<p>Espero haberte sido de gran ayuda, si te gusto mi servicio, me harías muy feliz calificándome<p>",
                null, "calification",999);
    }

    public ContentResponse closeChat(Map<String,String> inputs) {
        return new ContentResponse(
                "<p>Chat cerrado.<p>",
                null, null,1);
    }

    public ContentResponse inactiveChat(Map<String,String> inputs) {
        String chatId = inputs.get("chatId");
        Chat chat = chatSessionManager.getChatById(chatId);
        chat.setChatAuthenticated(false);
        chatSessionManager.setChatById(chatId,chat);
        return responseTimeOut;
    }




    public String generateCode(){
        String CARACTERES = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        return random.ints(6, 0, CARACTERES.length())
                .mapToObj(i -> String.valueOf(CARACTERES.charAt(i)))
                .reduce((a, b) -> a + b)
                .orElse("");
    }
}
