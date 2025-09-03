package com.microcode.client.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


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

    public String getDataAppsheets() {
        try {
            return this.webClient.post()
                    .uri(urlAuthorization + "/connect-appsheets")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Excepción en connectAppshest: " + e.getClass() + " - " + e.getMessage());
            return null;
        }
    }

//    public String getDataAppsheets() {
//        String jsonBody = """
//        {
//          "Action": "Find",
//          "Properties": {},
//          "Rows": [
//            {
//              "id": "1"
//            }
//          ]
//        }
//        """;
//
//        try {
//            String rawResponse = this.webClient.post()
//                    .uri("https://api.appsheet.com/api/v2/apps/e1ff3b19-9e78-424b-90e8-bb53b008f9ef/tables/TeoBienestar/records")
//                    .header("ApplicationAccessKey", "V2-4cj01-H7tDy-X7vFK-Q25Le-cM7yc-uWfNC-sQ83B-JLRRA")
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .bodyValue(jsonBody)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(rawResponse);
//
//            if (root.isArray() && !root.isEmpty() && root.get(0).has("Actividad")) {
//                return root.get(0).get("Actividad").asText();
//            } else {
//                return null;
//            }
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//
//    }

    public void ping() {
        try{
            this.webClient.post()
                    .uri(urlAuthorization + "/ping")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(RestClientException.class, ex -> Mono.empty()).subscribe();
        } catch (Exception ignored) {
        }
    }


   }