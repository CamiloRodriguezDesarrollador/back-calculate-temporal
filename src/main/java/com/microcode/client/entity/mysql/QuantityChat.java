package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_quantity_chat")
@Getter
@Setter
public class QuantityChat implements Serializable {

    @Id
    @Column(name = "QTC_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer quantityId;

    @Column(name = "TDC_TD")
    private String typeDocument;

    @Column(name = "EPL_ND")
    private String document;

    @Column(name = "AQTC_ID")
    private Integer actionId;

    @Column(name = "AUD_DATE", nullable = false)
    private Date audDate;


    @Serial
    private static final long serialVersionUID = 1L;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }
}
