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
@Table(name="CTOREGCOB ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entities implements Serializable {

    @Id
    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "TDC_TD")
    private String tdcTd;


    @Column(name = "CTO_NUMERO")
    private Long ctoNumero;


    @Column(name = "TEN_SIGLA")
    private String tenSigla;


    @Column(name = "EMP_ND_ENT")
    private Long empNdEnt;

    @Column(name = "TDC_TD_ENT")
    private String tdcTdEnt;

}
