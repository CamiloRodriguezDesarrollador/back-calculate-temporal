package com.microcode.client.entity.mysql;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.general.Option;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="plcht_status_chat")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusChat implements Serializable, Cloneable {

    @Id
    @Column(name = "CHT_ID")
    private String chatId;

    @Column(name = "CHT_MESSAGE", length = 2000)
    private String chatMessage;

    @Column(name = "CHT_OPTIONS", length = 2000)
    private String chatOptions;

    @Column(name = "CHT_HISTORY")
    private String isHistory;

    @Column(name = "CHT_ACTION")
    private Integer chatAction;

    @Column(name = "CHT_TYPE")
    private Integer chatType;

    @Column(name = "CHT_STATUS")
    private String chatStatus;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public StatusChat clone() {
        try {
            return (StatusChat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static StatusChat defineStatusInitial(String chatId, Integer companyId){

        List<Option> options = List.of(
                new Option(1,   "1. 👷 Trabajador / Extrabajador",        null, null),
                new Option(200, "2. 🏡 Cliente / Proveedor / Candidato",  null, null)
        );

        String urlData = "https://bit.ly/4qDempL";
        if(companyId == 2 ) urlData = "https://bit.ly/4qbL55W";
        if(companyId == 3 ) urlData = "https://bit.ly/4ssotj6";

        return StatusChat.builder()
                .chatId(chatId)
                .chatMessage(
                        "*¡Hola 👋!* Soy *Teo*, tu asistente virtual 🤖✨.\n" +
                                "¡Estoy aquí para ayudarte! 😊\n\n" +
                                "*Por favor elige una de las siguientes opciones:* 🙌\n\n" +
                                "1. 👷 Trabajador / Extrabajador\n" +
                                "2. 🏡 Cliente / Proveedor / Candidato / Externo \n\n"+
                                "Al continuar das autorización para el tratamientos de datos personales de acuerdo con la Política de datos. " + urlData + " ." + "\n\n"+
                                "👉 Para salir del chat en cualquier momento y volver aquí, escribe *EXIT*"
                )
                .chatOptions(options.toString())
                .audDate(new Date())
                .isHistory("N")
                .build();
    }

    public static StatusChat defineStatusStarted(String chatId, Chat chat, ContentResponse responseWrap, Integer typeChat) {
        boolean content = chat.getDocument() != null && chat.getTypeDocument() != null;

        List<Option> optionsYesOrNot = List.of(
                new Option(content ? 2 : 1, "Si", "Y", null),
                new Option(content ? 2 : 1, "No", "N", null)
        );

        List<Option> optionsNumber = List.of(
                new Option(50, "Enviame el código de verificación - Te pueden responder solo con el numero, o 'Es xxx código', y la action aca siempre es 50", null, null)
        );

        List<Option> optionsText = List.of(
                new Option(551, "Por favor, escríbeme un breve mensaje indicando lo que necesitas - Te pueden colocar cualquier cosa, siempre si o si responde bien con la accion 551, y detail el mensaje que coloquen", null, null)
        );

        if (responseWrap.getOptions() != null && !responseWrap.getOptions().isEmpty()) {
            List<Option> optionsTemporal = responseWrap.getOptions();
            for (int i = 0; i < optionsTemporal.size(); i++) {
                Option opt = optionsTemporal.get(i);
                String msg = opt.getActionMessage().replaceFirst("^\\d+\\.\\s*", "");
                opt.setActionMessage((i + 1) + ". " + msg);
            }
            responseWrap.setOptions(optionsTemporal);
        }


        List<Option> options = switch (responseWrap.getActionRequest()) {
            case "check"  -> optionsYesOrNot;
            case "number" -> optionsNumber;
            case "text" -> optionsText;
            default       -> (responseWrap.getOptions() != null && !responseWrap.getOptions().isEmpty())
                    ? responseWrap.getOptions()
                    : Collections.emptyList();
        };

        return StatusChat.builder()
                .chatId(chatId)
                .chatMessage(responseWrap.toString())
                .chatOptions(options.isEmpty() ? null : options.toString())
                .chatAction(Objects.equals(typeChat, 1) ? 1 : 200)
                .chatType(typeChat)
                .audDate(new Date())
                .chatStatus("C")
                .isHistory("S")
                .build();
    }


}
