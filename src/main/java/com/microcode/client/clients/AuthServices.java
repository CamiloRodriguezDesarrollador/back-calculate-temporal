package com.microcode.client.clients;

import com.microcode.client.secutiry.Env;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class AuthServices {

    @Value("${auth.api.url}")
    public String urlToken;
    public WebClient webClient;

    public AuthServices() {
        this.webClient = WebClient.builder()
                .build();
    }

    public String findMail() {
        try{
            return this.webClient.post()
                    .uri(urlToken+"/findMail")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }
    public Integer findClient() {
        try{
            return this.webClient.post()
                    .uri(urlToken+"/findClient")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }
    public Boolean validateToken() {
        try{
            return this.webClient.post()
                    .uri(urlToken+"/validateToken")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        }catch (Exception e) {
            return false;
        }
    }

    public Integer findType() {
        Map<String, Object> requestBody = new HashMap<>();
        try{

        return this.webClient.post()
                .uri(urlToken+"/findType")
                .header("Authorization", "Bearer " + Env.getCurrentToken())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }


}
