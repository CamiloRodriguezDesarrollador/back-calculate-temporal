package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_action")
@Getter
@Setter
@ToString
public class Action implements Serializable, Cloneable {


    @Id
    @Column(name = "ACT_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer actionId;

    @Column(name = "ACT_NAME_FUNCTION" , length = 100, nullable = false)
    private String actionNameFunction;

    @Column(name = "ACT_MESSAGE", length = 250)
    private String actionMessage;

    @Column(name = "ACT_DAYS_QUANTITY")
    private Integer actionDaysQuantity;

    @Column(name = "ACT_TYPE", length = 100)
    private String actionType;

    @Column(name = "ACT_RESP_OK_MESSAGE", length = 250)
    private String actionRespOkMessage;

    @Column(name = "ACT_RESP_OK_MESSAGE_PPAL", length = 250)
    private String actionRespOkMessagePrincipal;

    @Column(name = "ACT_RESP_OK_ACTION")
    private Integer actionRespOkAction;

    @Column(name = "ACT_RESP_OK_REQUEST", length = 50)
    private String actionRespOkRequest;

    @Column(name = "ACT_RESP_OK_FILE", length = 250)
    private String actionRespOkFile;

    @Column(name = "ACT_RESP_FAIL_MESSAGE", length = 250)
    private String actionRespFailMessage;

    @Column(name = "ACT_RESP_FAIL_ACTION")
    private Integer actionRespFailAction;

    @Column(name = "ACT_RESP_FAIL_REQUEST", length = 50)
    private String actionRespFailRequest;

    @Column(name = "ACT_RESP_FAIL_FILE", length = 250)
    private String actionRespFailFile;

    @Column(name = "ACT_QUANTITY", length = 10, nullable = false)
    private Integer actionQuantity;

    @Column(name = "ACT_STATUS", length = 10, nullable = false)
    private String actionStatus;

    @Column(name = "ACT_CTO_ACTIVE", length = 10, nullable = false)
    private String actionCtoActive;

    @Column(name = "ACT_OPTION", length = 50, nullable = false)
    private String actionOption;

    @Column(name = "ACT_OPTION_ERROR", length = 100, nullable = false)
    private String actionOptionError;

    @Column(name = "ACT_SIGLA", length = 50, nullable = false)
    private String actionSigla;

    @Column(name = "ACT_SIGLA_PPAL", length = 50, nullable = false)
    private String actionSiglaPrincipal;

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
    public Action clone() {
        try {
            return (Action) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


}
