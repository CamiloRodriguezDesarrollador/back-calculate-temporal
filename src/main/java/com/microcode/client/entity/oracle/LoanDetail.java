package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="DETPRESTAMO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoanDetail implements Serializable {

    @Id
    @Column(name = "PRE_CODIGO")
    private Long id;

    @Column(name = "PRO_CODIGO")
    private Long process;

    @Column(name = "DPR_PERIODO")
    private Long period;

    @Column(name = "DPR_CONSECUTIVO")
    private Long consecutive;

    @Column(name = "DPR_FECINI")
    private LocalDateTime dateIni;

    @Column(name = "DPR_FECFIN")
    private LocalDateTime dateEnd;

    @Column(name = "CPO_CODIGO_CAP")
    private Long conceptCap;

    @Column(name = "CPO_CODIGO_INT")
    private Long conceptInt;

    @Column(name = "DPR_FORMULA")
    private String metric;

    @Column(name = "DPR_FORMULA_CAP")
    private String metricCap;

    @Column(name = "DPR_FORMULA_INT")
    private String metricInt;

    @Column(name = "DPR_VALOR")
    private Long value;

    @Column(name = "DPR_ESTADO")
    private String status;

    @Column(name = "DPR_CUOTAS")
    private Long quotas;

    @Column(name = "DPR_ALTURA")
    private Long height;

    @Column(name = "MPR_SIGLA")
    private String siglaMpr;

    @Column(name = "DPR_DIAINI")
    private Long dayIni;

    @Column(name = "DPR_DIAFIN")
    private Long dayEnd;

    @Column(name = "DPR_RANGOFECHA")
    private String dateRange;

    @Column(name = "GRB_CODIGO")
    private Long codeGrb;

    @Column(name = "DPR_TIPO")
    private String typeDrp;

    @Column(name = "DPR_FECHANODESC")
    private LocalDateTime date;

    @Column(name = "DPR_CAPITAL_ANT")
    private String captAnt;

    @Column(name = "DPR_OBSERVACION")
    private String observation;


}
