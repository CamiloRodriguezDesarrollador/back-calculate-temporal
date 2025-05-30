package com.microcode.client.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="INCAPACIDAD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Incapacity {

    @Id
    @Column(name = "INC_NUMERO")
    private Long incNumero;

    @Column(name = "CTO_NUMERO")
    private Long ctoNumber;

    @Column(name = "TDC_TD_PAL")
    private String tdcTd;

    @Column(name = "EMP_ND_PAL")
    private Long empNd;

    @Column(name = "INC_ESTADO")
    private String incStatus;

    @Column(name = "INC_FECINI")
    private Date incInit;

    @Column(name = "INC_FECFIN")
    private Date incEnd;

    @Column(name = "INC_DIAS")
    private String incDays;

    @Column(name = "HPR_CONSEC")
    private String hprCon;


}
