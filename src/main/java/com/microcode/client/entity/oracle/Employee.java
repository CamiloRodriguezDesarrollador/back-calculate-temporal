package com.microcode.client.entity.oracle;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name="EMPLEADO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    @Id
    @Column(name = "EPL_ND")
    private Long eplNd;

    @Column(name = "TDC_TD")
    private String tdcTd;

    @Column(name = "EPL_EMAIL")
    private String email;

    @Column(name = "EPL_NOM1")
    private String emplName;

    @Column(name = "EPL_APELL1")
    private String empLastName;


}
