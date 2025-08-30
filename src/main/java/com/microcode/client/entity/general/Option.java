package com.microcode.client.entity.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Option {

    private Integer actionId;
    private String actionMessage;
    private String detail;
    private String actionInternal;

}