package com.microcode.client.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppSheets {

    Long _RowNumber;
    String concepto;
    Double   valor;
}
