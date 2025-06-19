package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_whatsapp")
@Getter
@Setter
@ToString
public class Whatsapp implements Serializable, Cloneable {

    @Id
    @Column(name = "WSP_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer whatsappId;

    @Column(name = "WSP_NUMBER")
    private String whatsappNumber;

    @Column(name = "CHAT_ID")
    private String chatId;

    @Column(name = "ACT_ID")
    private String actionId;

    @Column(name = "CHAT_AUTHENTICATED")
    private String chatAuthenticated;

    @Column(name = "CHAT_CODE")
    private String chatCode;

    @Column(name = "WSP_TYPE_DOCUMENT")
    private String whatsappTypeDocument;

    @Column(name = "WSP_DOCUMENT")
    private String whatsappDocument;

    @Column(name = "WSP_MAIL")
    private String whatsappMail;

    @Column(name = "WSP_IS_MAIL")
    private String whatsappIsMail;

    @Column(name = "WSP_MESSAGE")
    private String whatsappMessage;

    @Column(name = "WSP_STATUS", length = 10, nullable = false)
    private String whatsappStatus;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;

    @Column(name = "AUD_USER" ,length = 100)
    private String audUser;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Whatsapp clone() {
        try {
            return (Whatsapp) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


}
