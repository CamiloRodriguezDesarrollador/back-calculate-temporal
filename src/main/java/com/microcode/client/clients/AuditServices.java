package com.microcode.client.clients;

import com.microcode.client.entity.clients.Audit;
import com.microcode.client.secutiry.Env;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class AuditServices {


    @Value("${audit.api.url}")
    public String urlAudit;

    public WebClient webClient;

    public AuditServices() {
        this.webClient = WebClient.builder()
                .build();
    }


    public void create(Audit audit) {
        try {
            this.webClient.post()
                    .uri(urlAudit + "/create")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(audit)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty())
                    .subscribe();
        } catch (Exception ignored) {
        }
    }



}