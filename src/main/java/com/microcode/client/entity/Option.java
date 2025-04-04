package com.microcode.client.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Option {

    private Integer actionId;
    private String actionMessage;
    private String detail;

}