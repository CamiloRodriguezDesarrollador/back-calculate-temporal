package com.microcode.client.entity.mysql;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class StatusChatId implements Serializable {
    private String chatId;
    private Integer companyId;

    public StatusChatId() {}

    public StatusChatId(String chatId, Integer companyId) {
        this.chatId = chatId;
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusChatId that = (StatusChatId) o;
        return Objects.equals(chatId, that.chatId) &&
                Objects.equals(companyId, that.companyId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyId);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
