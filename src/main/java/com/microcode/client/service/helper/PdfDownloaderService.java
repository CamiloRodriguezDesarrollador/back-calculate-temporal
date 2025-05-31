package com.microcode.client.service.helper;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PdfDownloaderService {

    private final WebClient webClient;

    public PdfDownloaderService() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    public byte[] getPdfBytesPlanilla(Long tpqCode) {
        String uri = "https://apps.genialw.com/SitioTrabajador/ServletDownloadFileMiPlanilla?TPQ_CODE=" + tpqCode;

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

    public byte[] getPdfBytesDian(String uri) {
        System.out.println(uri);
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
                        return response.bodyToMono(String.class)
                                .doOnNext(errorBody -> {
                                    System.err.println("Error HTTP: " + response.statusCode());
                                    System.err.println("Cuerpo del error: " + errorBody);
                                })
                                .then(Mono.empty());
                    }
                })
                .block();
    }



}

