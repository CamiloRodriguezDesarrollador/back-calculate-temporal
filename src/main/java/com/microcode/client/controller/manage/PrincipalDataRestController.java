package com.microcode.client.controller.manage;

import com.microcode.client.entity.mysql.PrincipalData;
import com.microcode.client.secutiry.Env;
import com.microcode.client.secutiry.Handler.RequireMail;
import com.microcode.client.service.mysql.PrincipalDataServices;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat/principal-data")
public class PrincipalDataRestController {

    private PrincipalDataServices principalDataServices;

    @PostMapping("/dataTable")
    public List<PrincipalData> dataTable(@RequestParam(defaultValue = "1") Integer numberPage ,
                                  @RequestParam(defaultValue = "5") Integer numberElementPage ,
                                  @RequestParam(defaultValue = "") String text,
                                  @RequestParam(defaultValue = "A") String status) {
        return principalDataServices.findTableData(status, text, numberPage, numberElementPage);
    }

    @PostMapping("/quantity")
    public Integer quantity(@RequestParam(defaultValue = "") String text,
                            @RequestParam(defaultValue = "A") String status) {
        return principalDataServices.findTableQuantity(status, text);
    }


    @PostMapping("/create")
    @RequireMail
    public String create(@RequestBody PrincipalData act ){
        PrincipalData principalData = principalDataServices.findByEmpNdSigla(act.getEmpNd(),act.getPrincipalSigla());
        if (principalData == null) {
            act.setPrincipalStatus("A");
            act.setAudUser(Env.getCurrentMail());
            try {
                principalDataServices.create(act);
            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1)
                    return "it_already_exists";
                throw new RuntimeException(e.getMessage());
            }
            return "created";
        }
        return "it_already_exists";
    }


    @PutMapping("")
    public String updated(@RequestBody PrincipalData act ){
        PrincipalData principalData = this.principalDataServices.findPrincipalDataById(act.getPrincipalDataId());
        if (principalData != null) {
            principalData.setEmpNd(act.getEmpNd());
            principalData.setPrincipalSigla(act.getPrincipalSigla());
            principalData.setPrincipalValue(act.getPrincipalValue());
            principalData.setPrincipalDefault(act.getPrincipalDefault());
            principalData.setPrincipalStatus(act.getPrincipalStatus());

            try {
                principalDataServices.create(principalData);
            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 )
                    return "it_already_exists";
                throw new RuntimeException(e.getMessage());
            }
            return "edited";
        }
        return "not_found";
    }

}
