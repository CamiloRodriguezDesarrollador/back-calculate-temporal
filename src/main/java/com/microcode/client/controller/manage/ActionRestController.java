package com.microcode.client.controller.manage;

import com.microcode.client.service.mysql.ActionServices;
import com.microcode.client.service.mysql.PrincipalDataServices;
import com.microcode.client.service.mysql.QuestionServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/action")
public class ActionRestController {

    private final PrincipalDataServices principalDataServices;
    private final QuestionServices questionServices;
    private ActionServices actionServices;


    @PutMapping("/actions")
    public boolean updateActions() {
        try {
            actionServices.updateTypesChat();
            principalDataServices.updateDataPrincipal();
            questionServices.updateQuestionCalification();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @PostMapping("/ping")
    public boolean ping() {
        return true;
    }

}