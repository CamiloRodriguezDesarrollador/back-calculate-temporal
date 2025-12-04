package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name="plcht_status_chat")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusChat implements Serializable, Cloneable {

    @Id
    @Column(name = "CHT_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String chatId;

    @Column(name = "CHT_MESSAGE", length = 2000)
    private String chatMessage;

    @Column(name = "CHT_OPTIONS", length = 2000)
    private String chatOptions;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public StatusChat clone() {
        try {
            return (StatusChat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


}
