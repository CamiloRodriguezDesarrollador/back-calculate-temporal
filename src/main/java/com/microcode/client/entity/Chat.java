package com.microcode.client.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Chat {

    private String chatId;
    private Boolean chatAuthenticated;
    private String chatCode;
    private String typeDocument;
    private String document;
    private String chatMail;
    private String chatIp;
    private Date chatDateAuthorized;

    public Chat() {
    }
    public Chat(String chatId) {
        this.chatId = chatId;
    }

}