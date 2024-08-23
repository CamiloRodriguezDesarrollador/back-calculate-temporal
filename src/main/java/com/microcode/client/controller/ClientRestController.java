package com.microcode.client.controller;

import com.microcode.client.clients.AuditServices;
import com.microcode.client.clients.AuthServices;
import com.microcode.client.clients.AuthorizationServices;
import com.microcode.client.entity.Client;
import com.microcode.client.entity.clients.Audit;
import com.microcode.client.entity.clients.Authorization;
import com.microcode.client.secutiry.Env;
import com.microcode.client.secutiry.Handler.RequireMail;
import com.microcode.client.secutiry.Handler.RequireType;
import com.microcode.client.service.ClientServicesI;
import com.microcode.client.service.RegexServiceI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/client")
@ResponseStatus(HttpStatus.OK)
@AllArgsConstructor
public class ClientRestController {

    private final ClientServicesI clientServices;
    private final RegexServiceI regexService;
    private final AuthServices authServices;
    private final AuthorizationServices authorizationServices;
    private final AuditServices auditServices;
    private final Env env;
    private final HttpServletRequest request;


    @GetMapping("/allInformation")
    public List<Object> allInformation() {
        return clientServices.findAll();
    }

    @PostMapping("/dataTable")
    public List<Client> findQuantity(@RequestParam(defaultValue = "1") Integer numberPage ,
                                     @RequestParam(defaultValue = "5") Integer numberElementPage ,
                                     @RequestParam(defaultValue = "") String text,
                                     @RequestParam(defaultValue = "A") String status) {
        return clientServices.findTableData(status, text, numberPage, numberElementPage);
    }

    @PostMapping("/quantity")
    public Integer quantity(@RequestParam(defaultValue = "") String text,
                                      @RequestParam(defaultValue = "A") String status) {
        Integer quantity = clientServices.findTableQuantity(status, text);
        return Objects.requireNonNullElse(quantity, 0);
    }

    @PostMapping("/delete")
    @RequireMail
    public String delete(@RequestParam Integer cliId) {
        Client client = clientServices.findForIdentity(cliId, "A");
        if (client != null) {
            client.setCliStatus("I");
            clientServices.updated(client);
            auditServices.create(new Audit("/delete","client",client.toString(), Env.getCurrentMail(), request.getMethod(),
                    request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));
            return "deleted";
        }
        return "not_found";
    }

    @PostMapping("/active")
    @RequireMail
    public String active(@RequestParam Integer cliId) {
        Client client = clientServices.findForIdentity(cliId, "I");
        if (client != null) {
            client.setCliStatus("A");
            clientServices.updated(client);
            auditServices.create(new Audit("/active","client",client.toString(), Env.getCurrentMail(), request.getMethod(),
                    request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));
            return "activated";
        }
        return "not_found";
    }

    @PostMapping("/findFilter")
    public List<Client> findFilter(@RequestParam(defaultValue = "") String text) {
        return clientServices.findFilter("A", text);
    }

    @PostMapping("/findClientForCode")
    public Integer findClientForCode(@RequestParam String code) {
        Client client = clientServices.findWithCode(code);
        return (client != null) ? client.getCliId() : null;
    }

    @PostMapping("/findForId")
    public Client findForId(@RequestParam Integer cliId) {
        return clientServices.findForIdentity(cliId, "A");
    }

    @PostMapping("/findDataPrincipal")
    public List<Client> findDataClient() {
        List<Authorization> myAuth = authorizationServices.findMyAuthorization();
        if(myAuth == null) return null;
        Set<Integer> cliIdSet = new HashSet<>();
        List<Client> clients = new ArrayList<>();
        for (Authorization aut : myAuth) {
            Integer cliId = aut.getCliId();
            if (!cliIdSet.contains(cliId)) {
                cliIdSet.add(cliId);
                Client client = clientServices.findForIdentity(cliId, "A");
                Client myClient = new Client();
                myClient.setCliName(client.getCliName());
                myClient.setCliId(client.getCliId());
                myClient.setCliNd(client.getCliNd());
                clients.add(myClient);
            }
        }
        return clients;
    }

    @PostMapping("/findDataProfile")
    public Client findDataProfile() {
        return clientServices.findForIdentity(Env.getCurrentClient(), "A");
    }

    @PostMapping("/findMyClient")
    public Object findMyClient() {
        Client clients = clientServices.findForIdentity(Env.getCurrentClient(), "A");
        if(clients==null) return "unauthorized";
        Client myClient = new Client();
        myClient.setCliName(clients.getCliName());
        myClient.setCliId(clients.getCliId());
        myClient.setCliNd(clients.getCliNd());
        myClient.setCliColorSide(clients.getCliColorSide());
        myClient.setCliColorLetter(clients.getCliColorLetter());
        myClient.setCliLogo(clients.getCliLogo());
        myClient.setCliColorBack(clients.getCliColorBack());
        return myClient;
    }

    @PostMapping("/findClient")
    public Integer findClient() {
        return Env.getCurrentClient();
    }

    @PostMapping("/create")
    @RequireMail
    public String create(@RequestBody Client cli) {
        try{
            if (!regexService.isClient(cli) ) return "incorrect_fields";
        } catch (Exception e) {
            throw new RuntimeException("incorrect_fields " + e.getMessage());
        }
        Client myClientCode = clientServices.findWithCode(cli.getCliCode());
        if (myClientCode != null) return  "code_is_used";
        Client client = clientServices.findForName(cli.getCliName(), "A");
        if (client == null) {
            cli.setCliStatus("A");
            cli.setAudUser(Env.getCurrentMail());
            try {
                clientServices.create(cli);
            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1)
                    return "it_already_exists";
                throw new RuntimeException(e.getMessage());
            }
            return "created";
        }
        client.setCliStatus("A");
        clientServices.updated(client);
        return "it_already_exists";
    }

    @PostMapping("/updatedFolder")
    @RequireMail
    public String updatedFolder(@RequestParam Integer cliId,
                                @RequestParam String cliFolder) {
        Client client = clientServices.findForIdentity(cliId, "A");
        if(client == null || cliFolder == null)  return "not_found";
        client.setCliFolder(cliFolder);
        clientServices.updated(client);
        auditServices.create(new Audit("/updatedFolder","client",client.toString(), Env.getCurrentMail(), request.getMethod(),
                request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));
        return "updated";
    }

    @PostMapping("/updatedFolderInternal")
    @RequireMail
    public String updatedFolderInternal(@RequestParam Integer cliId,
                                                   @RequestParam String cliFolder) {
        Client clients = clientServices.findForIdentity(cliId, "A");
        clients.setCliFolderInternal(cliFolder);
        clientServices.updated(clients);
        auditServices.create(new Audit("/updatedFolder","client",clients.toString(), Env.getCurrentMail(), request.getMethod(),
                request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));
        return "updated";
    }

    @PostMapping("/updated")
    @RequireMail
    @RequireType
    public String update(@RequestBody Client cli) {
        if(!Objects.equals(authServices.findType(), env.typeClient)) return "unauthorized";
        Client client = clientServices.findForIdentity(Env.getCurrentClient(), "A");
        try{
            if (!regexService.isClientPar(cli)) return "incorrect_fields";
        } catch (Exception e) {

            throw new RuntimeException("incorrect_fields " + e.getMessage());
        }

        Client myClient = clientServices.findWithCode(cli.getCliCode());
        if(myClient.getCliId() !=  cli.getCliId()) return "code_is_used";

        if (client != null) {
//                      cliente.setCliLogo(cliLogo);
            client.setPaiName(cli.getPaiName());
            client.setDptName(cli.getDptName());
            client.setCiuName(cli.getCiuName());
            client.setCliAddress(cli.getCliAddress());
            client.setCliCellPhone(cli.getCliCellPhone());
            client.setCliColorSide(cli.getCliColorSide());
            client.setCliColorLetter(cli.getCliColorLetter());
            client.setCliColorBack(cli.getCliColorBack());

            try {
                clientServices.updated(client);
                auditServices.create(new Audit("/","client",client.toString(), Env.getCurrentMail(), request.getMethod(),
                        request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));

            } catch (DataIntegrityViolationException e) {
                if (((SQLException) e.getCause().getCause()).getErrorCode() == 1062 || ((SQLException) e.getCause().getCause()).getErrorCode() == 1 ) {
                    return "it_already_exists";
                }
                throw new RuntimeException(e.getMessage());
            }
            return "edited";
        }
        return "not_found";
    }

    @PutMapping("")
    @RequireMail
    public String updated(@RequestBody Client cli) {
        try{
            if (!regexService.isClient(cli) ) return "incorrect_fields";
        } catch (Exception e) {
            throw new RuntimeException("incorrect_fields " + e.getMessage());
        }
        Client myClient = clientServices.findWithCode(cli.getCliCode());
        if(myClient != null)if(myClient.getCliId() !=  cli.getCliId()) return "code_is_used";
        Client client = clientServices.findForIdentity(cli.getCliId(), "A");
        if (client != null) {
            client.setCliName(cli.getCliName());
            client.setTdcTdGroup(cli.getTdcTdGroup());
            client.setCliNdGroup(cli.getCliNdGroup());
            client.setCliPrincipal(cli.getCliPrincipal());
//                cliente.setCliLogo(cliLogo);
            client.setPaiName(cli.getPaiName());
            client.setDptName(cli.getDptName());
            client.setCiuName(cli.getCiuName());
            client.setCliCode(cli.getCliCode());
            client.setCliAddress(cli.getCliAddress());
            client.setCliCellPhone(cli.getCliCellPhone());
            client.setCliColorSide(cli.getCliColorSide());
            client.setCliColorLetter(cli.getCliColorLetter());
            client.setCliColorBack(cli.getCliColorBack());
            try {
                clientServices.updated(client);
                auditServices.create(new Audit("/update","client",client.toString(), Env.getCurrentMail(), request.getMethod(),
                        request.getHeader("X-Forwarded-For"),request.getHeader("User-Agent")));
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
