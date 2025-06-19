package com.microcode.client.controller.manage;

import com.microcode.client.entity.mysql.Whatsapp;
import com.microcode.client.entity.oracle.Employee;
import com.microcode.client.secutiry.Env;
import com.microcode.client.service.helper.HelperService;
import com.microcode.client.service.mysql.WhatsappServices;
import com.microcode.client.service.oracle.EmployeeServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/wp")
public class WpChatRestController {

    private final EmployeeServices employeeServices;
    private final HelperService helperService;
    private WhatsappServices whatsappServices;

    @GetMapping("/status")
    public Whatsapp dataTable(@RequestParam() String whatsappNumber) {

        Whatsapp wp = whatsappServices.findByNumberAndStatus(whatsappNumber,"A");
        if(wp == null) {
            Whatsapp newWp = new Whatsapp();
            newWp.setWhatsappNumber(whatsappNumber);
            newWp.setWhatsappStatus("A");
            whatsappServices.create(newWp);
            return newWp;
        }
        return wp;
    }


    @PostMapping("")
    public Whatsapp create(@RequestBody Whatsapp wsp ){
        Whatsapp wp = whatsappServices.findByNumberAndStatus(wsp.getWhatsappNumber(),"A");
        if (wp == null) {
            wsp.setWhatsappStatus("A");
            whatsappServices.create(wsp);
            return wsp;
        }else{
            if (wsp.getWhatsappDocument() != null && !wsp.getWhatsappDocument().isEmpty()) wp.setWhatsappDocument(wsp.getWhatsappDocument());
            if (wsp.getWhatsappTypeDocument() != null &&  !wsp.getWhatsappTypeDocument().isEmpty()) wp.setWhatsappTypeDocument(wsp.getWhatsappTypeDocument());
            if (wsp.getChatCode() != null && !wsp.getChatCode().isEmpty()) wp.setChatCode(wsp.getChatCode());
            if (wsp.getWhatsappIsMail() != null && !wsp.getWhatsappIsMail().isEmpty()) wp.setWhatsappIsMail(wsp.getWhatsappIsMail());
            if (wsp.getWhatsappMail() != null &&  !wsp.getWhatsappMail().isEmpty())  wp.setWhatsappMail(wsp.getWhatsappMail());

            if(wsp.getWhatsappDocument() != null && wsp.getWhatsappTypeDocument() != null) {
                String doc = wsp.getWhatsappDocument();

                if (!doc.trim().isEmpty()) {

                    Long id = Long.valueOf(doc);
                    Employee employee = employeeServices.findByIds(id, "CC");
                    if(employee != null && employee.getEmail() != null){
                        System.out.println("no debe entrar");
                        wp.setWhatsappMail(helperService.generateMail(employee.getEmail().toLowerCase()));
                    }else {
                        wp.setWhatsappMail("sin correo");
                    }
                }


            }


            if(wsp.getChatCode() != null && (wp.getWhatsappIsMail() != null && wp.getWhatsappIsMail().equals("s")) && wsp.getActionId() == null){

                // Validar código
                wp.setChatAuthenticated("s");
                wp.setWhatsappMessage("""
                    Estas autenticado correcto, puedes solicitar las siguientes opciones
                    1. Ultimo certificado laboral 📄
                    2. Estado de mi liquidación 📢
                    3. Instructivo Retiro de cesantias 📏
                   """);
            }

            System.out.println("llega aca");

            if(wp.getChatAuthenticated() != null && wp.getChatAuthenticated().equals("s") && wsp.getActionId() != null){
                System.out.println("llega acasasa");
                System.out.println(wsp.getActionId());
                switch (wsp.getActionId()) {
                    case "1":
                        wp.setWhatsappMessage(
                                "✅ Tu certificado laboral ha sido generado exitosamente y enviado a tu correo electrónico registrado. 📩\n\n" +
                                        "Si no lo ves en tu bandeja de entrada, recuerda revisar tu carpeta de spam o correo no deseado."
                        );
                        break;

                    case "2":
                        wp.setWhatsappMessage(
                                "🔄 Tu proceso de liquidación ya está en marcha. 👷‍♀️\n\n" +
                                        "Te notificaremos por este medio y por correo una vez esté finalizado. Agradecemos tu paciencia."
                        );
                        break;

                    case "3":
                        wp.setWhatsappMessage(
                                "📚 Para gestionar tus cesantías, te compartimos el instructivo con los pasos a seguir.\n\n" +
                                        "Asegúrate de seguir cada paso con atención y no dudes en contactarnos si tienes preguntas."
                        );
                        break;

                    default:
                        wp.setWhatsappMessage(
                                "⚠️ Lo sentimos, aún no contamos con una opción para esa solicitud.\n\n" +
                                        "Por favor intenta con una opción diferente o escríbenos para ayudarte mejor."
                        );
                        break;
                }

            }


            whatsappServices.create(wp);

            return wp;
        }

    }


}
