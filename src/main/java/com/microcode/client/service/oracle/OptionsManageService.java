package com.microcode.client.service.oracle;

import com.microcode.client.entity.general.Option;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.entity.mysql.Question;
import com.microcode.client.service.mysql.ActionServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
@AllArgsConstructor
@Setter
@Getter
public class OptionsManageService {

    public final ActionServices actionServices;

    public static List<Action> actionsPrincipal;
    public static List<Option> optionsBasic;
    public static List<Option> optionsBasicExternal;
    public static List<Option> optionsEps;
    public static List<Option> optionsBienestar;
    public static List<Option> Bienestar;
    public static List<Option> optionsPrincipal;
    public static List<Option> optionsPrincipalExternal;
    public static List<Option> optionsDocument;
    public static List<Option> optionsEntities;
    public static List<Option> optionEndChat;
    public static List<Option> optionsLiq;
    public static List<Option> optionsPension;
    public static List<Option> optionsInca;
    public static List<Option> optionsCCF;
    public static List<Option> optionsFedac;
    public static List<Option> optionsPortal;
    public static List<Option> optionsCandidato;
    public static List<Option> optionsClientePotencial;
    public static List<Question> questionsCalification;

    public static List<PrincipalData> dataActionsPrincipal;


    public static void updateActions(List<Action> actions) {
        actionsPrincipal = actions;
    }
    public static void updateDataActionsPrincipal(List<PrincipalData> dataActions) {
        dataActionsPrincipal = dataActions;
    }
    public static void updateQuestionsCalification(List<Question> questions) {
        questionsCalification = questions;
    }

    public static void updateOptionsBasic(List<Option> options) {
        optionsBasic = options;
    }
    public static void updateOptionsBasicExternal(List<Option> options) {
        optionsBasicExternal = options;
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
    public static void updateOptionsFedac(List<Option> options) {
        optionsFedac = options;
    }
    public static void updateOptionsClientePotencial(List<Option> options) {
        optionsClientePotencial = options;
    }
    public static void updateOptionsPortal(List<Option> options) {
        optionsPortal = options;
    }
    public static void updateOptionsCandidato(List<Option> options) {
        optionsCandidato = options;
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
    public static void updateOptionsPrincipalExternal(List<Option> options) {
        optionsPrincipalExternal = options;
    }

    @SuppressWarnings("unchecked")
    public static List<Option> getOptionsByActionWithName(String fieldName) {
        try {
            Field field = OptionsManageService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (List<Option>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Option> getOptionsByActionWithOption(String type) {
        return actionServices.updateOptionsByType(type,null);
    }



}
