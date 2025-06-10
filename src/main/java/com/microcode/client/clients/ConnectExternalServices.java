package com.microcode.client.clients;

import com.microcode.client.entity.clients.Authorization;
import com.microcode.client.secutiry.Env;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class ConnectExternalServices {


    @Value("${connect.external.api.url}")
    public String urlAuthorization;

    public WebClient webClient;

    public ConnectExternalServices() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    public byte[] connectUrl(String url) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("url", url);
        try {
            return this.webClient.post()
                    .uri(urlAuthorization + "/connect")
                    .bodyValue(params)
                    .exchangeToMono(response -> {
                        System.out.println("Status code: " + response.statusCode());
                        System.out.println("Content-Type: " + response.headers().contentType());

                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(byte[].class);
                        } else {
                            return response.bodyToMono(String.class)
                                    .doOnNext(body -> System.out.println("Respuesta de error: " + body))
                                    .then(Mono.empty());
                        }
                    })
                    .block();
        } catch (Exception e) {
            System.out.println("Excepción en connectUrl: " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }


   }