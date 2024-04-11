package com.microcode.client.entity.clients;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class Authorization implements Serializable {

    @Id
    private int autId;
    private Integer usuId;
    private Integer cliId;
    private Integer appId;
    private Integer perId;
    private Integer tipId;
    private String autStatus;
    private Date audDate;
    private String audUser;
    @Transient
    private Integer proId;

}
