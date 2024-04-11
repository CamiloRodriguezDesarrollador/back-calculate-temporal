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
public class DocumentServices {


    @Value("${document.api.url}")
    public String urlDocument;

    public WebClient webClient;

    public DocumentServices() {
        this.webClient = WebClient.builder()
                .build();
    }
    public String createFolder(String idFolderPrincipal, String nameFolder, Boolean isPublic) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("idFolderPrincipal", idFolderPrincipal);
        params.add("nameFolder", nameFolder);
        params.add("isPublic", isPublic);
        try{
            return this.webClient.post()
                    .uri(urlDocument+"/createFolder")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean ping() {
        try{
            return this.webClient.post()
                    .uri(urlDocument+"/ping")
                    .header("Authorization", "Bearer " + Env.getCurrentToken())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).block();
        } catch (Exception e) {
            return null;
        }
    }



}