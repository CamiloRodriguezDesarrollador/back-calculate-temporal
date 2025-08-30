package com.microcode.client.entity.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentMessage {

    private String chatOwnerMessage;
    private Map<String,String> chatMessage;
    private Integer actionId;


    @Override
    public String toString() {
        StringBuilder chatMessageString = new StringBuilder();
        if (chatMessage != null) {
            chatMessage.forEach((key, value) -> chatMessageString.append(key).append("=").append(value).append(", "));
            if (!chatMessageString.isEmpty()) {
                chatMessageString.setLength(chatMessageString.length() - 2);
            }
        }
        return "ContentMessage{" +
                "chatOwnerMessage='" + chatOwnerMessage + '\'' +
                ", chatMessage={" + chatMessageString + "}" +
                ", actionId=" + actionId +
                '}';
    }

    public String toStringMessage() {
        StringBuilder chatMessageString = new StringBuilder();
        if (chatMessage != null && !chatMessage.isEmpty()) {
            chatMessage.forEach((key, value) -> {
                chatMessageString.append(key).append("=").append(value).append(", ");
            });
            if (!chatMessageString.isEmpty()) {
                chatMessageString.setLength(chatMessageString.length() - 2);
            }
        } else {
            chatMessageString.append("No messages available");
        }
        return chatMessageString.toString();
    }

}