package com.microcode.client.controller.manage;

import com.microcode.client.entity.mysql.RegisterChat;
import com.microcode.client.service.mysql.RegisterChatServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/register")
public class RegisterChatRestController {

    private RegisterChatServices RegisterChatServices;

    @PostMapping("/dataTable")
    public List<RegisterChat> dataTable(@RequestParam(defaultValue = "1") Integer numberPage ,
                                  @RequestParam(defaultValue = "5") Integer numberElementPage ,
                                  @RequestParam(defaultValue = "") String text) {
        return RegisterChatServices.findTableData( text, numberPage, numberElementPage);
    }

    @PostMapping("/quantity")
    public Integer quantity(@RequestParam(defaultValue = "") String text) {
        return RegisterChatServices.findTableQuantity(text);
    }


}
