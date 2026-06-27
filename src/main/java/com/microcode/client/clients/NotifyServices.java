package com.microcode.client.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotifyServices {

    @Value("${notify.api.url}")
    public String urlNotify;

    @Value("${api.key.notify}")
    public String apyKeyNotify;

    public Mono<Void> sendNotifyChat(String text) {
        WebClient webClient = WebClient.builder().build();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("channel", "CHAT");
        body.add("principal", 860090915);
        body.add("to", "AAQANpyZhBM");
        body.add("message", text);
        body.add("process","18" );

        return webClient.post()
                .uri(urlNotify + "/send")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("X-Current-Token",apyKeyNotify)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody ->Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(String.class)
                .doOnError(ex ->
                        log.error("ERROR: {}" , ex.getMessage())
                )
                .then();
    }
}