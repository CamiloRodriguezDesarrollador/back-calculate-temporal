package com.microcode.client.service.mysql;

import com.microcode.client.dao.mysql.IRegisterChatDao;
import com.microcode.client.entity.Chat;
import com.microcode.client.entity.ContentMessage;
import com.microcode.client.entity.ContentResponse;
import com.microcode.client.entity.mysql.RegisterChat;
import com.microcode.client.service.chat.ChatSessionManager;
import com.microcode.client.service.helper.HelperService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegisterChatServices implements RegisterChatServicesI {

    private final IRegisterChatDao registerChatDao;
    private final ChatSessionManager chatSessionManager;
    private final HttpServletRequest request;
    private final HelperService helperService;

    @Override
    public void create(RegisterChat registerChat) {
        registerChatDao.save(registerChat);
    }

    @Override
    public void createForMessage(String chatId, ContentMessage contentMessage, String ip, Integer companyId, Integer typeChat) {
        Chat chat = chatSessionManager.getChatById(chatId);
        String codePrincipal = helperService.definePrincipalForCode(companyId).toString();

        try{
            RegisterChat reg = new RegisterChat();
            reg.setChatId(chatId);
            reg.setTypeDocument(chat.getTypeDocument());
            reg.setDocument(chat.getDocument());
            reg.setChatOwnerMessage(contentMessage.getChatOwnerMessage());
            reg.setChatMessage(contentMessage.toStringMessage());
            reg.setActionId(contentMessage.getActionId());
            reg.setChatPrincipal( codePrincipal);
            reg.setChatIp(ip);
            reg.setCtoActive(chat.getContractActive() ? "A" : "I");


            reg.setEmpNdFil(chat.getEmpNdFil());
            reg.setCtoNumber(chat.getCtoNumber());
            reg.setTypeChat(typeChat);
            create( reg );

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createForResponse(String chatId, ContentResponse contentResponse, String ip, Integer companyId, Integer typeChat) {
        Chat chat = chatSessionManager.getChatById(chatId);
        String codePrincipal = helperService.definePrincipalForCode(companyId).toString();

        try {
            RegisterChat reg = new RegisterChat();
            reg.setChatId(chatId);
            reg.setTypeDocument(chat.getTypeDocument());
            reg.setDocument(chat.getDocument());
            reg.setChatOwnerMessage("bot");
            reg.setChatPrincipal(codePrincipal);
            reg.setChatMessage(contentResponse.getActionMessage() +
                    (contentResponse.getOptions() != null ? " , Options: " + contentResponse.getOptions() : ""));
            reg.setActionId(contentResponse.getActionId());
            reg.setChatIp(ip);
            reg.setCtoActive(chat.getContractActive() ? "A" : "I");


            reg.setEmpNdFil(chat.getEmpNdFil());
            reg.setCtoNumber(chat.getCtoNumber());
            reg.setTypeChat(typeChat);

            create( reg );
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public List<RegisterChat> findTableData(String text, Integer numberPage, Integer numberElementPage) {
        if(numberPage == null) numberPage = 1;
        else if (numberPage<1)  numberPage = 1;
        Pageable pageable = PageRequest.of(numberPage - 1, numberElementPage, Sort.Direction.DESC, "id");
        return registerChatDao.findTableData(text.toLowerCase(), pageable);
    }

    @Override
    public Integer findTableQuantity(String text) {
        return registerChatDao.findTableQuantity(text);
    }

}
