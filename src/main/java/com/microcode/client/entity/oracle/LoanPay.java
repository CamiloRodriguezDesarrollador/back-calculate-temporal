package com.microcode.client.entity.oracle;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanPay {
    private long preCodigo;
    private double preSaldo;
    private double preValor;
    private String trpNombre;
}
