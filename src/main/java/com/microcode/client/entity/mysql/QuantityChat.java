package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_quantity_chat_desa")
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

    @Column(name = "ACT_ID")
    private Integer actionId;

    @Column(name = "ACT_DETAIL", length = 100)
    private String actionDetail;

    @Column(name = "ACT_PPAL")
    private String actionPrincipal;

    @Column(name = "AUD_DATE", nullable = false)
    private Date audDate;

    @Serial
    private static final long serialVersionUID = 1L;

    @Transient
    private String actionName;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    public QuantityChat(Integer quantityId, String typeDocument, String document, Integer actionId, Date audDate, String actionName) {
        this.quantityId = quantityId;
        this.typeDocument = typeDocument;
        this.document = document;
        this.actionId = actionId;
        this.audDate = audDate;
        this.actionName = actionName;
    }

    public QuantityChat() {
    }
}
