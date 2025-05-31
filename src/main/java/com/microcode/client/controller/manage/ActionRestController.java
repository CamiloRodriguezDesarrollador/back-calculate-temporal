package com.microcode.client.controller.manage;

import com.microcode.client.entity.mysql.Action;
import com.microcode.client.secutiry.Env;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.PrincipalDataServices;
import com.microcode.client.service.mysql.RegexService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import com.microcode.client.secutiry.Handler.RequireMail;

import java.sql.SQLException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/action")
public class ActionRestController {

    private final PrincipalDataServices principalDataServices;
    private ActionServices actionServices;
    private RegexService regexService;

    @PostMapping("/dataTable")
    public List<Action> dataTable(@RequestParam(defaultValue = "1") Integer numberPage ,
                                  @RequestParam(defaultValue = "5") Integer numberElementPage ,
                                  @RequestParam(defaultValue = "") String text,
                                  @RequestParam(defaultValue = "A") String status) {
        return actionServices.findTableData(status, text, numberPage, numberElementPage);
    }

    @PostMapping("/quantity")
    public Integer quantity(@RequestParam(defaultValue = "") String text,
                            @RequestParam(defaultValue = "A") String status) {
        return actionServices.findTableQuantity(status, text);
    }

    @PutMapping("/actions")
    public boolean updateActions() {
        try {
            actionServices.updateTypesChat();
            principalDataServices.updateDataPrincipal();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/create")
    @RequireMail
    public String create(@RequestBody Action act ){
        try{
            if (!regexService.isActionCorrect(act)) return "incorrect_fields";
        } catch (Exception e) {
            throw new RuntimeException("incorrect_fields " + e.getMessage());
        }
        Action action = actionServices.findByMessageAndStatus(act.getActionMessage(), "A");
        if (action == null) {
            act.setActionStatus("A");
            act.setAudUser(Env.getCurrentMail());
            try {
                actionServices.create(act);
            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1)
                    return "it_already_exists";
                throw new RuntimeException(e.getMessage());
            }
            return "created";
        }
        action.setActionStatus("A");
        this.actionServices.create(action);
        return "it_already_exists";
    }


    @PutMapping("")
    public String updated(@RequestBody Action act ){
        Action action = this.actionServices.findActionById(act.getActionId());
        if (action != null) {
            action.setActionMessage(act.getActionMessage());
            action.setActionType(act.getActionType());
            action.setActionDaysQuantity(act.getActionDaysQuantity());
            action.setActionQuantity(act.getActionQuantity());
            action.setActionNameFunction(act.getActionNameFunction());
            action.setActionRespOkMessage(act.getActionRespOkMessage());
            action.setActionRespOkMessagePrincipal(act.getActionRespOkMessagePrincipal());
            action.setActionRespOkAction(act.getActionRespOkAction());
            action.setActionRespOkRequest(act.getActionRespOkRequest());
            action.setActionRepOkMail(act.getActionRepOkMail());
            action.setActionRepOkMailSubject(act.getActionRepOkMailSubject());
            action.setActionRespFailMessage(act.getActionRespFailMessage());
            action.setActionRespFailAction(act.getActionRespFailAction());
            action.setActionRespFailRequest(act.getActionRespFailRequest());
            action.setActionStatus(act.getActionStatus());
            action.setActionCtoActive(act.getActionCtoActive());
            action.setActionOption(act.getActionOption());
            action.setActionOptionError(act.getActionOptionError());
            action.setActionSigla(act.getActionSigla());
            action.setActionSiglaPrincipal(act.getActionSiglaPrincipal());

            try {
                actionServices.create(action);
            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 )
                    return "it_already_exists";
                throw new RuntimeException(e.getMessage());
            }
            return "edited";
        }
        return "not_found";
    }

}
