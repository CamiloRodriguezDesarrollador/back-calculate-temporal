package com.microcode.client.entity.oracle;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name="TELEFEPL")
@IdClass(EmployeePhoneId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePhone implements Serializable {

    @Id
    @Column(name = "EPL_ND")
    private Long eplNd;

    @Id
    @Column(name = "TDC_TD")
    private String tdcTd;

    @Id
    @Column(name = "TEP_NUMERO")
    private String number;


}
