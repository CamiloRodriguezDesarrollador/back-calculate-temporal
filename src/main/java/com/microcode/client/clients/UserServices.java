package com.microcode.client.clients;

import com.microcode.client.secutiry.Env;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class UserServices {

    @Value("${user.api.url}")
    public String urlUser;

    public WebClient webClient;

    public UserServices() {
        this.webClient = WebClient.builder()
                .build();
    }

    public Integer findForMail(String mail) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("mail", mail);
        try{
            return this.webClient.post()
                    .uri(urlUser +"/findForMail")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }

}