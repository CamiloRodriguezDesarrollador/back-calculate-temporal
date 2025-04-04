package com.microcode.client.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MailServices {

    @Value("${mail.api.url}")
    public String urlMail;

    public WebClient webClient;

    public MailServices() {
        this.webClient = WebClient.builder()
                .build();
    }

    @Async
    public Integer sendMailVerified(String emailTo,  String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("emailTo", emailTo);
        params.add("code", code);
        try{
            return this.webClient.post()
                    .uri(urlMail +"/sendMailVerified")
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }

}
