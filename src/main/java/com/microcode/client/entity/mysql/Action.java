package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PLCHT_ACTION")
@Getter
@Setter
public class Action implements Serializable {


    @Id
    @Column(name = "ACT_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer actionId;

    @Column(name = "ACT_NAME" , length = 100, nullable = false)
    private String actionName;

    @Column(name = "ACT_MESSAGE", length = 250)
    private String actionMessage;

    @Column(name = "ACT_QUANTITY", length = 10, nullable = false)
    private Integer actionQuantity;

    @Column(name = "ACT_STATUS", length = 10, nullable = false)
    private String actionStatus;

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


}
