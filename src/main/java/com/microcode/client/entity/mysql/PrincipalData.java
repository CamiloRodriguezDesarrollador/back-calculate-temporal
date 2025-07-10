package com.microcode.client.entity.mysql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="plcht_principal_data_desa")
@Getter
@Setter
@ToString
public class PrincipalData implements Serializable, Cloneable {

    @Id
    @Column(name = "PPD_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer principalDataId;

    @Column(name = "EMP_ND" )
    private Long empNd;

    @Column(name = "PPD_SIGLA", length = 20)
    private String principalSigla;

    @Column(name = "PPD_VALUE", length = 100)
    private String principalValue;

    @Column(name = "PPD_DEFAULT", length = 1)
    private String principalDefault;

    @Column(name = "PPD_STATUS", length = 10, nullable = false)
    private String principalStatus;

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
    public PrincipalData clone() {
        try {
            return (PrincipalData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


}
