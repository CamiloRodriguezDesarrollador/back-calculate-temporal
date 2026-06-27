package com.microcode.client.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcode.client.entity.AppSheets;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ConnectedServices {

    private final WebClient webClient;

    public ConnectedServices() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
    public List<AppSheets> getDataAppsheets() {
        String jsonBody = """
                {
                  "Action": "Find",
                  "Properties": {},
                  "Rows": []
                }
                """;

        try {
            String rawResponse = this.webClient.post()
                    .uri("https://api.appsheet.com/api/v2/apps/7b6135f8-6edf-4fd4-b2b1-1d1e5e8dc2f5/tables/Premisas/records")
                    .header("ApplicationAccessKey", "V2-h9gjX-aKbHB-Ib6FG-Ystm9-a5nVa-KK59M-N2MTE-Zfo4G")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(jsonBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawResponse);
            List<AppSheets> responseList = new ArrayList<>();

            if (root != null && root.isArray()) {
                for (JsonNode row : root) {
                    Long rowNumber = row.has("_RowNumber") && !row.get("_RowNumber").isNull() ? row.get("_RowNumber").asLong() : null;
                    String concepto = row.has("CONCEPTO") && !row.get("CONCEPTO").isNull() ? row.get("CONCEPTO").asText() : null;
                    Double valor = row.has("VALOR") && !row.get("VALOR").isNull() ? row.get("VALOR").asDouble() : null;
                    AppSheets appSheetRecord = AppSheets.builder()
                            ._RowNumber(rowNumber)
                            .concepto(concepto)
                            .valor(valor)
                            .build();

                    responseList.add(appSheetRecord);
                }
                return responseList;
            }

            return Collections.emptyList();

        } catch (Exception e) {
            System.out.println("Error al consultar AppSheet: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Mono<String> sendToAppSheet(Map<String, Object> data) {

        String apiUrl = "https://api.appsheet.com/api/v2/apps/7b6135f8-6edf-4fd4-b2b1-1d1e5e8dc2f5/tables/Registros/records";
        String accessKey = "V2-h9gjX-aKbHB-Ib6FG-Ystm9-a5nVa-KK59M-N2MTE-Zfo4G";

        Map<String, Object> body = Map.of(
                "Action", "Add",
                "Properties", Map.of(
                        "Locale", "es-CO",
                        "Timezone", "America/Bogota"
                ),
                "Rows", List.of(data)
        );

        return this.webClient.post()
                .uri(apiUrl)
                .header("ApplicationAccessKey", accessKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.out.println("Error al insertar en AppSheet: " + e.getMessage()))
                .onErrorReturn("{\"error\": \"Error al procesar la inserción\"}");
    }



}
