package com.microcode.client.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="histnov")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HistNovCompany {

    @Id
    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "tdc_td")
    private String tdcTd;

    @Column(name = "cto_numero")
    private Long ctoNumero;

    @Column(name = "pmg_fecinih")
    private LocalDate pmgFecini;

    @Column(name = "pro_codigo")
    private Long proCodigo;


}
