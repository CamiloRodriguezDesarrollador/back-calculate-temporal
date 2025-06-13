package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_register_chat")
@Getter
@Setter
public class RegisterChat implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CHAT_ID")
    private String chatId;

    @Column(name = "TDC_TD")
    private String typeDocument;

    @Column(name = "EPL_ND")
    private String document;

    @Column(name = "ACT_ID")
    private Integer actionId;

    @Column(name = "CHAT_OWNER_MESSAGE" , length = 100, nullable = false)
    private String chatOwnerMessage;

    @Column(name = "CHAT_MESSAGE", length = 250, nullable = false)
    private String chatMessage;

    @Column(name = "AUD_DATE", nullable = false)
    private Date audDate;

    @Column(name = "CHAT_IP", nullable = false)
    private String chatIp;

    @Column(name = "CHAT_PRINCIPAL", nullable = false)
    private String chatPrincipal;

    @Column(name = "EPM_ND_FIL")
    private Long empNdFil;

    @Column(name = "CTO_NUMBER")
    private Long ctoNumber;


    @Serial
    private static final long serialVersionUID = 1L;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }
}
