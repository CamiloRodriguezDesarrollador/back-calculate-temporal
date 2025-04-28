package com.microcode.client.entity.oracle;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterPay {
    private String concepto;
    private BigDecimal cantidad;
    private BigDecimal valor;
    private String tipo;
}
