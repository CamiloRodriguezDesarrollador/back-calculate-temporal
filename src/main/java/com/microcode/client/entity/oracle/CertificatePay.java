package com.microcode.client.entity.oracle;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertificatePay {
    private String nombreEmpresa;
    private String nombreEmpresaFilial;
    private String periodoNomina;
    private Long consecutivoHpr;
    private String nombreEmpleado;
    private String identificacionEmpleado;
    private Long sueldo;
    private String ipp;
    private Long rtefte;
    private String centroCosto;
    private String nombreCiudad;
    private String mensajeBasico;
    private String cesantias;
    private String mensaje;
    private String otroMensaje;
    private String monto;
    private String mensaje2;
    private List<RegisterPay> tablaPago = new ArrayList<>();
    private List<LoanPay> tablaAhorroPrestamo = new ArrayList<>();
    private String error;
}
