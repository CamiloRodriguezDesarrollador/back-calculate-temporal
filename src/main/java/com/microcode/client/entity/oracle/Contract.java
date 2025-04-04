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
import java.util.Date;

@Entity
@Table(name="CONTRATO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contract implements Serializable {

    @Id
    @Column(name = "CTO_NUMERO")
    private Long ctoNumero;

    @Column(name = "EPL_ND")
    private Long eplNd;

    @Column(name = "TDC_TD_EPL")
    private String tdcTdEpl;

    @Column(name = "CTO_FECING")
    private Date ctoIng;

}
