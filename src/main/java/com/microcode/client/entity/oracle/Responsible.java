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
@Table(name="RESPONSABLE_INTERNO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Responsible implements Serializable {

    @Id
    @Column(name = "EMP_ACU_CODIGO")
    private Long empAcuCodigo;

    @Column(name = "RIN_MAIL")
    private String rinMail;

    @Column(name = "TCA_CODIGO")
    private Long tcaCodigo;
}
