package com.microcode.client.service.helper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PdfDownloaderService {

    private final WebClient webClient;

    public PdfDownloaderService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://apps.genialw.com")
                .build();
    }

    public byte[] getPdfBytes(Long tpqCode) {
        String uri = "/SitioTrabajador/ServletDownloadFileMiPlanilla?TPQ_CODE=" + tpqCode;

        return webClient.get()
                .uri(uri)
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "identity")
                .header("Connection", "keep-alive")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(byte[].class);
                    } else {
                        return Mono.empty();
                    }
                })
                .block();
    }


}

