package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name="EMPRESA_ACUERDO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAgreement implements Serializable {

    @Id
    @Column(name = "EMP_ACU_CODIGO")
    private Long empAcuCodigo;

    @Column(name = "EMP_ND_FIL")
    private Long empNdFil;

    @Column(name = "TDC_TD_FIL")
    private String tdcTdFil;






}
