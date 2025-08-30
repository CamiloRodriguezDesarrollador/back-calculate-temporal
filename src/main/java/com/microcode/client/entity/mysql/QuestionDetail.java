package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_question_detail")
@Getter
@Setter
@ToString
public class QuestionDetail implements Serializable {

    @Id
    @Column(name = "QUD_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer questionDetailId;

    @Column(name = "QUD_NAME" , length = 100, nullable = false)
    private String questionDetailName;

    @Column(name = "QUE_ID", nullable = false)
    private Integer questionId;

    @Column(name = "QUD_STATUS", length = 10, nullable = false)
    private String questionDetailStatus;

    @Column(name = "QUD_ORDER", length = 50, nullable = false)
    private Integer questionDetailOrder;

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
