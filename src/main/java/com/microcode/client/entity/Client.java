package com.microcode.client.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PLIGR_CLIENT")
@Getter
@Setter
public class Client implements Serializable {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "CLI_ID")
    private int cliId;

    @Column(name = "TDC_TD" , length = 30)
    private String tdcTd;

    @Column(name = "CLI_ND", length = 50)
    private String cliNd;

    @Column(name = "TDC_TD_GROUP" , length = 30)
    private String tdcTdGroup;

    @Column(name = "CLI_ND_GROUP" ,length = 30)
    private String cliNdGroup;

    @Column(name = "CLI_PRINCIPAL" ,length = 10)
    private String cliPrincipal;

    @Column(name = "CLI_NAME" ,length = 250)
    private String cliName;

    @Column(name = "CLI_CODE" ,length = 100)
    private String cliCode;

    @Column(name = "CLI_LOGO" ,length = 150)
    private String cliLogo;

    @Column(name = "PAI_NAME" ,length = 30)
    private String paiName;

    @Column(name = "DPT_NAME" ,length = 30)
    private String dptName;

    @Column(name = "CIU_NAME" ,length = 30)
    private String ciuName;

    @Column(name = "CLI_ADDRESS" ,length = 100)
    private String cliAddress;

    @Column(name = "CLI_CELLPHONE" ,length = 30)
    private String cliCellPhone;

    @Column(name = "CLI_COLOR_SIDE" ,length = 30)
    private String cliColorSide;

    @Column(name = "CLI_COLOR_LETTER" ,length = 30)
    private String cliColorLetter;

    @Column(name = "CLI_COLOR_BACK" ,length = 30)
    private String cliColorBack;

    @Column(name = "CLI_FOLDER" ,length = 100)
    private String cliFolder;

    @Column(name = "CLI_FOLDER_INTERNAL" ,length = 100)
    private String cliFolderInternal;

    @Column(name = "CLI_STATUS" ,length = 10)
    private String cliStatus;

    @Column(name = "AUD_DATE" ,length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private Date audDate;

    @Column(name = "AUD_USER" ,length = 100)
    private String audUser;

    @PrePersist
    public void prePersist() {
        audDate = new Date();
    }


    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "{" +
                "cliId=" + cliId +
                ", tdcTd='" + tdcTd + '\'' +
                ", cliNd='" + cliNd + '\'' +
                ", tdcTdGroup='" + tdcTdGroup + '\'' +
                ", cliNdGroup='" + cliNdGroup + '\'' +
                ", cliPrincipal='" + cliPrincipal + '\'' +
                ", cliName='" + cliName + '\'' +
                ", cliCode='" + cliCode + '\'' +
                ", cliLogo='" + cliLogo + '\'' +
                ", paiName='" + paiName + '\'' +
                ", dptName='" + dptName + '\'' +
                ", ciuName='" + ciuName + '\'' +
                ", cliAddress='" + cliAddress + '\'' +
                ", cliCellPhone='" + cliCellPhone + '\'' +
                ", cliColorSide='" + cliColorSide + '\'' +
                ", cliColorLetter='" + cliColorLetter + '\'' +
                ", cliColorBack='" + cliColorBack + '\'' +
                ", cliFolder='" + cliFolder + '\'' +
                ", cliFolderInternal='" + cliFolderInternal + '\'' +
                ", cliStatus='" + cliStatus + '\'' +
                ", audDate=" + audDate +
                ", audUser='" + audUser + '\'' +
                '}';
    }
}
