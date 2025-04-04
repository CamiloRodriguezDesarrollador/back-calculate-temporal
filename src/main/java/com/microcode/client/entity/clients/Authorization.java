package com.microcode.client.entity.clients;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PLIGR_AUTHORIZATION")
@Getter
@Setter
public class Authorization implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "AUT_ID")
    private int autId;

    @Column(name = "USU_ID")
    private Integer usuId;

    @Column(name = "CLI_ID")
    private Integer cliId;

    @Column(name = "APP_ID")
    private Integer appId;

    @Column(name = "PER_ID")
    private Integer perId;

    @Column(name = "TIP_ID")
    private Integer tipId;

    @Column(name = "AUT_STATUS" ,length = 10)
    private String autStatus;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;

    @Column(name = "AUD_USER" ,length = 100)
    private String audUser;

    @Transient
    private Integer proId;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    private static final long serialVersionUID = 1L;


}
