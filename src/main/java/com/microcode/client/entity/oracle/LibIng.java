package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="LIBROINGRESO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LibIng implements Serializable {

    @Id
    @Column(name = "CTO_NUMERO")
    private Long ctoNumero;

    @Column(name = "TDC_TD_PPAL")
    private String tdcTdPpal;

    @Column(name = "EMP_ND_PPAL")
    private Long empNdPpal;

    @Column(name = "SUC_NOMBRE_ADMIN")
    private String sucNameAdmin;


}
