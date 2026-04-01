package com.microcode.client.entity.oracle;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="DETTIPOPAGO")
@IdClass(TypePaymentFedacId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TypePaymentFedac implements Serializable {

    @Id
    @Column(name = "TPG_NOMBRE")
    private String typePaymentName;

    @Column(name = "PRO_CODIGO")
    private Long process;

    @Column(name = "DTP_CONSECUTIVO")
    private Long consecutive;

    @Column(name = "CPO_CODIGO_CAP")
    private Long codeCap;

    @Column(name = "CPO_CODIGO_INT")
    private Long codeInt;

    @Column(name = "DTP_FORMULA")
    private String metric;

    @Column(name = "DTP_FORMULA_CAP")
    private String metricCap;

    @Column(name = "DTP_FORMULA_INT")
    private String metricInt;

    @Column(name = "DTP_PERIODO")
    private Long period;

    @Column(name = "DTP_CUOTAS")
    private Long quotas;

    @Column(name = "MPR_SIGLA")
    private String siglaMpr;

    @Column(name = "DTP_DIAINI")
    private Long dayIni;

    @Column(name = "DTP_DIAFIN")
    private Long dayEnd;

    @Id
    @Column(name = "TDC_TD")
    private String typeDocument;

    @Id
    @Column(name = "EMP_ND")
    private Long document;

    @Column(name = "TEN_SIGLA")
    private String siglaTen;

    @Column(name = "CPO_CODIGO_PAGO")
    private Long codePayment;

    @Column(name = "DPT_RANGOFECHA")
    private String rangeDate;

    @Column(name = "DPT_TIPO")
    private String typeDpt;

    @Column(name = "DPT_CAPITAL_ANT")
    private String typeDptAnt;

}
