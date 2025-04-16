package com.microcode.client.entity;

import com.microcode.client.entity.mysql.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MessageInvoke {

    private Map<String,String> messageRequest;
    private Action action;

}