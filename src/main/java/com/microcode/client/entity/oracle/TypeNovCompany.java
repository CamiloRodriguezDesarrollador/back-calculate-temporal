package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="TIPONOV_EMPRESA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TypeNovCompany implements Serializable {

    @Id
    @Column(name = "TNE_CODIGO")
    private Long tneCodigo;

    @Column(name = "TNO_CODIGO")
    private Long tnoCodigo;

    @Column(name = "TDC_TD")
    private String tdcTd;

    @Column(name = "EMP_ND")
    private Long empNd;



}
