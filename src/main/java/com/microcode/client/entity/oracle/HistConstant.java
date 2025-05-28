package com.microcode.client.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="HISTCONST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HistConstant {

    @Id
    @Column(name = "EMP_ND_USU")
    private Long empNdFil;

    @Column(name = "cte_nombre")
    private String cteName;

    @Column(name = "hct_valor")
    private String hctValue;


}
