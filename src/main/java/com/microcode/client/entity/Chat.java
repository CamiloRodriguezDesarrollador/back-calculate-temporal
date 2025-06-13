package com.microcode.client.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

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
    private Date chatStart;
    private Date chatDateAuthorized;
    private String names;
    private Date chatDateCode;
    private Integer chatAttempts;
    private String tdcTdFil;
    private Long empNdFil;
    private Long ctoNumber;
    private String tdcTd;
    private Long empNd;
    private List<Long> principalRequest;
    private String perSigla;
    private String periodPlanilla;
    private Boolean contractActive;
    public String contractCompany;
    public String contractCity;



    public Chat() {
    }
    public Chat(String chatId) {
        this.chatId = chatId;
    }

}