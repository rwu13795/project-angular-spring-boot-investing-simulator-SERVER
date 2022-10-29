package com.raywu.investingsimulator.stock.search;

import com.raywu.investingsimulator.utility.EnvVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SearchService {
    private final WebClient webClient;
    private final String FMP_API_KEY;

    @Autowired
    public SearchService(WebClient.Builder webClientBuilder, EnvVariable env) {
        String FMP_API_URL = env.FMP_API_URL();
        this.FMP_API_KEY = env.FMP_API_KEY();

        webClient = webClientBuilder
                .baseUrl(FMP_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    String searchStock(String query, String exchange) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/search")
                        // ----- (1) ----- //
                        .queryParam("query", query)
                        .queryParam("exchange", exchange)
                        .queryParam("limit", 20)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
