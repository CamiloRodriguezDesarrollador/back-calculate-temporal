package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="EMPRESA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Company implements Serializable {

    @Id
    @Column(name = "EMP_ND")
    private Long empNd;

    @Column(name = "TDC_TD")
    private String tdcTd;

    @Column(name = "EMP_NOMBRE")
    private String empNombre;


}
