package com.microcode.client.entity.oracle;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="PRESTAMO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Loan implements Serializable {

    @Id
    @Column(name = "PRE_CODIGO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TDC_TD")
    private String principalTypeDocument;

    @Column(name = "EMP_ND")
    private Long principalDocument;

    @Column(name = "CTO_NUMERO")
    private Long ctoNumber;

    @Column(name = "PRE_VALOR")
    private Long value;

    @Column(name = "PRE_SALDO_CAPITAL")
    private Long valueCapital;

    @Column(name = "PRE_SALDO_INTERES")
    private Long valueInterests;

    @Column(name = "PRE_ESTADO")
    private String status;

    @Column(name = "TPR_NOMBRE")
    private String typeName;

    @Column(name = "TPG_NOMBRE")
    private String typePayment;

    @Column(name = "GAR_SIGLA")
    private String warranty;

    @Column(name = "PRE_AUTORIZACION")
    private String authorization;

    @Column(name = "PRE_DOCUMENTO")
    private String document;

    @Column(name = "PRE_TASA")
    private Long rate;

    @Column(name = "PRE_PERIODOS")
    private Long period;

    @Column(name = "PRE_ALTURA")
    private Long height;

    @Column(name = "PRE_CALCULADO")
    private String calculate;

    @Column(name = "PRE_VENCIDO")
    private String validity;

    @Column(name = "TDC_TD_ENT")
    private String entityTypeDocument;

    @Column(name = "EMP_ND_ENT")
    private Long entityDocument;

    @Column(name = "TEN_SIGLA")
    private String typeEntity;

    @Column(name = "PER_SIGLA")
    private String periodicity;

    @Column(name = "PRE_TIPO")
    private String typeLoan;

    @Column(name = "PRE_CONTAB")
    private String isCont;

    @Column(name = "PRE_CHEQUE")
    private Long isCheck;

    @Column(name = "PRE_CONTROL")
    private String control;

    @Column(name = "PRE_FECHA")
    private LocalDateTime date;

    @Column(name = "PRE_COMENTARIO")
    private String commentary;

    @Column(name = "TDC_TD_EPL")
    private String employeeTypeDocument;

    @Column(name = "EPL_ND")
    private Long employeeDocument;

    @Column(name = "TDC_TD_BEN")
    private String providerTypeDocument;

    @Column(name = "NIT_ND_BEN")
    private Long providerDocument;

    @Column(name = "CTB_CONSEC")
    private Long consecutive;

    @Column(name = "PRE_FECHDEV")
    private LocalDateTime dateDev;

}
