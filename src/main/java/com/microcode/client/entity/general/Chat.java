package com.microcode.client.entity.general;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class Chat {

    private String chatId;
    private Boolean chatAuthenticated;
    private String chatCode;
    private String typeDocument;
    private String document;
    private String chatMail;
    private String chatPhone;
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
    private Integer typeChat;
    private boolean chatActive;
    private String companyId;


    public Chat() {
    }
    public Chat(String chatId, String companyId) {
        this.chatId = chatId;
        this.companyId = companyId;
    }

}