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
@Table(name="aportenov")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContributionNov implements Serializable {

    @Id
    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "tdc_td")
    private String tdcTd;

    @Column(name = "ape_periodo")
    private String apePeriod;

    @Column(name = "apo_consec")
    private Long apoConsec;

    @Column(name = "pro_codigo")
    private Long proCodigo;

    @Column(name = "EPL_ND")
    private Long eplNd;

    @Column(name = "TDC_TD_EPL")
    private String tdcTdEpl;

}