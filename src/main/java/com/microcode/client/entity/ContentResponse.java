package com.microcode.client.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ContentResponse {

    private String actionMessage;
    private List<Option> options;
    private String actionRequest;
    private Integer actionId;

}