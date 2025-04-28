package com.microcode.client.entity.oracle;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertificateJob
{

    private String texto;
    private String nombreEmpresaPrincipal;
    private String nombreEmpresaFilial;
    private Long empresaNd;
    private String responsable;
    private String rolResponsable;
    private String areaResponsable;
    private String dirLogo;
    private String dirFirma;
    private String textoConfirmacion;
    private String pieInfEmpresa;
    private String textoLm;
    private String textoNota;
    private String textoTitulo;
    private String textoAtte;
    private String error;

}
