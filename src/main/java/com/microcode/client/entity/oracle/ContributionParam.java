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
import java.time.LocalDate;

@Entity
@Table(name="aporte_param")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContributionParam implements Serializable {

    @Id
    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "tdc_td")
    private String tdcTd;

    @Column(name = "apn_periodo")
    private String apnPeriod;

    @Column(name = "apo_consec")
    private Long apoConsec;

    @Column(name = "pro_codigo")
    private Long proCodigo;

    @Column(name = "num_pla")
    private String numPla;



}