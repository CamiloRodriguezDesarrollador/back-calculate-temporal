package com.microcode.client.entity.oracle;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentObject
{


    private String vCConcepto;
    private Integer nmCantidad;
    private Double nmValor;
    private String vcTipo;


}
