package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IActionDao;
import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.general.Option;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.manage.ManageServices;
import com.microcode.client.service.oracle.OptionsManageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActionServices implements ActionServicesI {

    public final IActionDao actionDao;

    @Override
    public void create(Action action) {
        actionDao.save(action);
    }

    @Override
    public Action findActionById(Integer actionId) {
        return actionDao.findActionByActionId(actionId);
    }

    @Override
    public List<Action> findByTypeAndStatus(String actionType, String status) {
        return actionDao.findByActionTypeAndActionStatus(actionType,status);
    }

    @Override
    public List<Action> findTableData(String status, String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null) numberPage = 1;
        else if (numberPage<1)  numberPage = 1;
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "actionId");
        return actionDao.findTableData(text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String status, String text) {
        return actionDao.findTableQuantity(text);
    }

    @Override
    public Action findByMessageAndStatus(String actionMessage, String status) {
        return actionDao.findByActionMessageAndActionStatus(actionMessage, status);
    }

    @Override
    public void updateTypesChat(){
        List<Action> actions = actionDao.findByActionStatusOrderByActionId("A");
        OptionsManageService.updateActions(actions);

        updateOptionsByType("principal", OptionsManageService::updateOptionsPrincipal);
        updateOptionsByType( "principal-external", OptionsManageService::updateOptionsPrincipalExternal);
        updateOptionsByType("basic", OptionsManageService::updateOptionsBasic);
        updateOptionsByType("basic-external", OptionsManageService::updateOptionsBasicExternal);
        updateOptionsById(OptionsManageService::updateOptionEndChat);
//        updateOptionsByType(actions, "documents", OptionsManageService::updateOptionsDocument);
//        updateOptionsByType(actions, "eps", OptionsManageService::updateOptionsEps);
//        updateOptionsByType(actions, "bienestar", OptionsManageService::updateOptionsBienestar);
//        updateOptionsByType(actions, "pension", OptionsManageService::updateOptionsPension);
//        updateOptionsByType(actions, "incapacidades", OptionsManageService::updateOptionsInca);
//        updateOptionsByType(actions, "ccf", OptionsManageService::updateOptionsCcf);
//        updateOptionsByType(actions, "fedac", OptionsManageService::updateOptionsFedac);
//        updateOptionsByType(actions, "portal-hv", OptionsManageService::updateOptionsPortal);
//        updateOptionsByType(actions, "candidato", OptionsManageService::updateOptionsCandidato);
//        updateOptionsByType(actions, "cliente-potencial", OptionsManageService::updateOptionsClientePotencial   );
        updateOptionsByType("liq", OptionsManageService::updateOptionsLiq);
//        updateOptionsByType(actions, "entities", OptionsManageService::updateOptionEntities);

        List<Option> optionsUnit = new ArrayList<>();
        optionsUnit.addAll(OptionsManageService.optionsPrincipal);
        optionsUnit.addAll(OptionsManageService.optionEndChat);

        ManageServices.unauthorized = buildResponse("unauthorized", null);
        ManageServices.notFound = buildResponse( "notFound", optionsUnit);
        ManageServices.noAction = buildResponse( "noAction", optionsUnit);
        ManageServices.timeOut = buildResponse( "timeOut", null);
        ManageServices.error = buildResponse( "error", optionsUnit);
        ManageServices.quantityMax = buildResponse( "quantityMax", optionsUnit);
        ManageServices.maxAttempts = buildResponse( "maxAttempts", null);
        ManageServices.withoutContract = buildResponse( "withoutContract", optionsUnit);
        ManageServices.otherSessionActive = buildResponse( "otherSessionActive", null);

    }

    @Override
    public ContentResponse buildResponse(String actionName, List<Option> secondParam) {

        List<Action> actions = actionDao.findByActionStatusOrderByActionId("A");

        return actions.stream()
                .filter(a -> actionName.equals(a.getActionNameFunction()))
                .findFirst()
                .map(a -> new ContentResponse(
                        a.getActionRespOkMessage(),
                        secondParam,
                        a.getActionRespOkRequest(),
                        a.getActionRespOkAction(),
                        a.getActionRedirect(),
                        a.getActionType(),
                        null
                ))
                .orElse(null);
    }

    @Override
    public List<Option> updateOptionsByType(String type, Consumer<List<Option>> updater) {
        List<Action> actions = actionDao.findByActionStatusOrderByActionId("A");

        List<Option> options = actions.stream()
                .filter(a -> type.equalsIgnoreCase(a.getActionType()))
                .sorted(Comparator.comparing(Action::getActionOrder))
                .map(a -> new Option(a.getActionId(), a.getActionMessage(), null, a.getActionId().toString()))
                .collect(Collectors.toList());

        if(updater != null) updater.accept(options);
        return options;
    }

    @Override
    public void updateOptionsById( Consumer<List<Option>> updater ) {
        List<Action> actions = actionDao.findByActionStatusOrderByActionId("A");

        List<Option> options = actions.stream()
                .filter(a -> ((Integer) 1000).equals(a.getActionId()))
                .sorted(Comparator.comparing(Action::getActionOrder))
                .map(a -> new Option(a.getActionId(), a.getActionMessage(), null, a.getActionId().toString()))
                .collect(Collectors.toList());

        updater.accept(options);
    }

    @Override
    public Action getActionForId(Integer actionId ){
        return OptionsManageService.actionsPrincipal.stream()
                .filter(e -> Objects.equals(e.getActionId(), actionId))
                .findFirst()
                .orElse(null);

    }

    @Override
    public boolean verifiedRequirementContractActive(Chat chat, Action action){
        if(action.getActionCtoActive() == null) return true;
        if(chat.getContractActive() != null  && chat.getContractActive()) return true;
        return !action.getActionCtoActive().equals("A") || !Boolean.FALSE.equals(chat.getContractActive());
    }

}
