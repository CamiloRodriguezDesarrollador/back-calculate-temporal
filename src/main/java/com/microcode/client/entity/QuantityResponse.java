package com.microcode.client.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class QuantityResponse {

    private LocalDate dateOptionTrain;
    private Boolean isOver;

}