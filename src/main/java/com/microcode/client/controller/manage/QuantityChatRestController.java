package com.microcode.client.controller.manage;

import com.microcode.client.entity.mysql.QuantityChat;
import com.microcode.client.service.mysql.QuantityChatServices;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/quantity")
public class QuantityChatRestController {

    private QuantityChatServices QuantityChatServices;

    @PostMapping("/dataTable")
    public List<QuantityChat> dataTable(@RequestParam(defaultValue = "1") Integer numberPage ,
                                  @RequestParam(defaultValue = "5") Integer numberElementPage ,
                                  @RequestParam(defaultValue = "") String text) {
        return QuantityChatServices.findTableData(text, numberPage, numberElementPage);
    }

    @PostMapping("/quantity")
    public Integer quantity(@RequestParam(defaultValue = "") String text) {
        return QuantityChatServices.findTableQuantity(text);
    }

    @PostMapping("/delete")
    public String updated(@RequestBody QuantityChat act ){
            try {
                QuantityChatServices.delete(Long.valueOf(act.getQuantityId()));
            } catch (Exception e) {
                return "external_error";
            }
            return "edited";
    }

}
