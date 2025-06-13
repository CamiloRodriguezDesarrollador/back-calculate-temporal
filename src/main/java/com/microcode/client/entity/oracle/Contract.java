package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="CONTRATO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contract implements Serializable {

    @Id
    @Column(name = "CTO_NUMERO")
    private Long ctoNumero;

    @Column(name = "TDC_TD_EPL")
    private String tdcTdEpl;

    @Column(name = "EPL_ND")
    private Long eplNd;

    @Column(name = "CTO_FECING")
    private Date ctoIng;

    @Column(name = "CTO_FECRET")
    private Date ctoEnd;

    @Column(name = "ECT_SIGLA")
    private String ectSigla;

    @Column(name = "TDC_TD_FIL")
    private String tdcTdFil;

    @Column(name = "EMP_ND_FIL")
    private Long empNdFil;

    @Column(name = "TDC_TD")
    private String tdcTd;

    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "PER_SIGLA")
    private String perSigla;


}
