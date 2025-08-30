package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="plcht_question")
@Getter
@Setter
@ToString
public class Question implements Serializable {


    @Id
    @Column(name = "QUE_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer questionId;

    @Column(name = "QUE_NAME" , length = 100, nullable = false)
    private String questionName;

    @Column(name = "QUE_TYPE" , length = 50, nullable = false)
    private String questionType;

    @Column(name = "ACT_ID", length = 50, nullable = false)
    private Integer actionId;

    @Column(name = "QUE_ORDER", length = 50, nullable = false)
    private Integer questionOrder;

    @Column(name = "QUE_STATUS", length = 10, nullable = false)
    private String questionStatus;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;

    @Column(name = "AUD_USER" ,length = 100)
    private String audUser;

    @Transient
    private List<QuestionDetail> questionsDetail;


    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }

    @Serial
    private static final long serialVersionUID = 1L;


}
