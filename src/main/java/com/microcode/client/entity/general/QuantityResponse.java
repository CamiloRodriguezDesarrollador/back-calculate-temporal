package com.microcode.client.entity.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class QuantityResponse {

    private LocalDate dateOptionTrain;
    private Boolean isOver;

}