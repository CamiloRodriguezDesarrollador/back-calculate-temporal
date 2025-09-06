package com.microcode.client.clients;

import com.microcode.client.secutiry.Env;
import com.microcode.client.service.helper.HelperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class MailServices {

    @Value("${mail.api.url}")
    public String urlMail;

    public WebClient webClient;
    public HelperService helperService;

    public MailServices() {
        this.helperService = new HelperService();
    }

    public void sendMailChat(String emailTo, String contentMail, String subject, List<Long>  authorized) {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("emailTo", emailTo);
        params.add("contentMail", contentMail);
        params.add("subject", subject);

        Long principalAuthorized = helperService.defineUniquePrincipalForAuthorized(authorized);
        params.add("principalAuthorized", principalAuthorized);
        try{
            this.webClient.post()
                    .uri(urlMail + "/sendMailChat")
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Mono<String> sendMailCertificatesFile(String contentMail, String subject , String emailTo, byte[] fileBytes, String filename,
                                                 List<Long> authorized) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("contentMail", contentMail);
        body.add("subject", subject);
        body.add("emailTo", emailTo);

        Long principalAuthorized = helperService.defineUniquePrincipalForAuthorized(authorized);
        body.add("principalAuthorized", principalAuthorized);

        if (fileBytes == null || fileBytes.length == 0) return Mono.empty();

        body.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        return this.webClient.post()
                .uri(urlMail + "/sendMailCertificatesFile")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(RestClientException.class, ex -> Mono.empty());
    }


    public void ping() {
        try{
            this.webClient.post()
                    .uri(urlMail + "/ping")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).subscribe();
        } catch (Exception ignored) {
        }
    }

}
