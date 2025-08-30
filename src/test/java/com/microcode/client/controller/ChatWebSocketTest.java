package com.microcode.client.controller;

import com.microcode.client.entity.general.Chat;
import com.microcode.client.entity.general.ContentMessage;
import com.microcode.client.entity.general.ContentResponse;
import com.microcode.client.entity.mysql.Action;
import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.oracle.ActionsOracleServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatWebSocketTest implements WebSocketMessageBrokerConfigurer {

    @LocalServerPort
    private int port;

    @Autowired
    private ActionsOracleServices actionsOracleServices;

    @Autowired
    private ActionServices actionServices;

    public String eplNd = "39655785";
    public String tdcTdEpl = "CC";
    public Long contract = 597706L;
    public Long empNdFil = 860090915L;
    public String typeChat = "1";
    public String detail = "597706-860090915-NI";

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();

        String url = "ws://localhost:" + port + "/api/chat/gs-guide-websocket";

        stompClient
                .connectAsync(url, handshakeHeaders, connectHeaders, new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);
    }

    @RepeatedTest(2090)
    public void getDataUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();

        chat.setChatId(String.valueOf(UUID.randomUUID()));
        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(1);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("document", eplNd );
        chatMessage.put("typeDocument", tdcTdEpl);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void verifiedMail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();

        chat.setChatId(String.valueOf(UUID.randomUUID()));
        chat.setChatStart(new Date());
        actionsOracleServices.initialChatIfNull(chat.getChatId());

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(2);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("isMailCorrect", "Y");
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void verified() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatDateCode(new Date());
        chatMockup.setChatCode("123456");
        chatMockup.setChatAttempts(1);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(50);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("codeVerification", "123456");
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);

        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");
        assertFalse(resp.getActionMessage().contains("El código enviado no es correcto"));

    }

    @Test
    public void getOptions() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        actionsOracleServices.initialChatIfNull(chat.getChatId());

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(500);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getCertifiedJob() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(501);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getCertifiedJobDetail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(502);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getDataIncludeEps() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(515);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getTransferEps() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(516);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getDataPsychology() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(519);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getInfRetiredCesa() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(508);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getDataConCesan() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(507);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getDataCesanAffiliation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(521);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getIncAffiliation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(522);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void requirementCcfPersonalizate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(530);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getDataRequirementsCcf() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException    {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(529);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getCertifiedPayDetail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(528);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMockup.setCtoNumber(contract);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getCertifiedDian() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(505);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getIncapacities() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(505);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");
    }

    @Test
    public void getFedacInformationContact() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(539);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }


    @Test
    public void getFedacInformationConvenio() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(540);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }


    @Test
    public void getFedacInformationOptico() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(541);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getInformationPortal() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(206);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getInformationClientPot() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(213);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    @Test
    public void getInformationProvider() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Chat chat = new Chat();
        Map<String, String> chatMessage = new HashMap<>();
        chat.setChatId(String.valueOf(UUID.randomUUID()));
        Chat chatMockup = actionsOracleServices.initialChatIfNull(chat.getChatId());
        chatMockup.setEmpNd(empNdFil);
        chatMockup.setChatStart(new Date());
        chatMockup.setChatDateAuthorized(new Date());
        chatMockup.setChatAuthenticated(true);

        ContentMessage mockMessage = new ContentMessage();
        mockMessage.setActionId(204);
        Action action = actionServices.getActionForId(mockMessage.getActionId());
        Method methodAction = actionsOracleServices.getClass().getMethod(  action.getActionNameFunction(), Map.class, Action.class);
        chatMockup.setDocument(eplNd);
        chatMockup.setTypeDocument(tdcTdEpl);
        chatMessage.put("detail", detail);
        chatMessage.put("chatId", chat.getChatId());
        chatMessage.put("typeChat", typeChat);
        mockMessage.setChatMessage(chatMessage);

        ContentResponse resp = (ContentResponse) methodAction.invoke(actionsOracleServices, mockMessage.getChatMessage(), action);
        assertNotNull(resp);
        assertNotEquals(resp.getActionType(),"error");

    }

    
}

