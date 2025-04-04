package com.microcode.client.controller;

import com.microcode.client.service.ChatSessionManager;
import com.microcode.client.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentMap;

@RestController
@AllArgsConstructor
public class ChatRestController {

    private ChatSessionManager chatSessionManager;

    @GetMapping("/active/chats")
    public ConcurrentMap<String, Chat> getActiveChats() {
        return chatSessionManager.getActiveChats();
    }


}
