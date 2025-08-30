package com.microcode.client.entity.general;

import com.microcode.client.entity.mysql.Action;
import com.microcode.client.entity.mysql.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ContentResponse {

    private String actionMessage;
    private List<Option> options;
    private String actionRequest;
    private Integer actionId;
    private String actionType;
    private List<Question> questions;

    public ContentResponse() {
    }

    public static ContentResponse buildContentResponseFail(String text, List<Option> options, Action action, List<Question> questions) {
        return new ContentResponse(
                text,
                options,
                action != null ? action.getActionRespFailRequest() : null,
                action != null ? action.getActionRespFailAction() : null,
                action != null ? action.getActionType() : null,
                questions
        );
    }

    public static ContentResponse buildContentResponseOk(String text, List<Option> options, Action action, List<Question> questions) {
        return new ContentResponse(
                text,
                options,
                action != null ? action.getActionRespOkRequest() : null,
                action != null ? action.getActionRespOkAction() : null,
                action != null ? action.getActionType() : null,
                questions
        );
    }

    public static ContentResponse cloneContentResponse(ContentResponse original) {
        try{
            if (original == null) return new ContentResponse();

            ContentResponse copy = new ContentResponse();
            copy.setActionMessage(original.getActionMessage());
            copy.setOptions(original.getOptions());
            copy.setActionRequest(original.getActionRequest());
            copy.setActionId(original.getActionId());
            copy.setActionType(original.getActionType());
            copy.setActionMessage(original.getActionMessage());
            copy.setQuestions(original.getQuestions());

            return copy;
        }catch (Exception e) {
            return original;
        }
    }


    public static ContentResponse buildNotMail(List<Option> options) {
        return new ContentResponse(
                "<p>No cuentas con un correo registrado 😟, por favor contactate con tu empresa, o intenta nuevamente 👇.<p/>",
                options,
                "verified",
                null,
                null,
                null
        );
    }





}