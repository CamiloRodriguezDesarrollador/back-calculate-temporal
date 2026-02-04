package com.microcode.client.clients;

import com.microcode.client.controller.secutiry.Env;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotifyServices {

    @Value("${notify.certificate.api.url}")
    public String urlNotify;

    public NotifyServices() {
    }

    @Async
    public void notifyChatApps(String text) {
        WebClient webClient = Env.withCurrentHeaders(WebClient.builder()).build();

        Map<String, String> body = new HashMap<>();
        body.put("spaceId", "AAQA1c87pDg");
        body.put("text", text);
        try {
            webClient.post()
                    .uri(urlNotify + "/send-notify-chat")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(ex -> Mono.empty())
                    .subscribe();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




}
