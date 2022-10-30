package com.raywu.investingsimulator.stock;

import com.raywu.investingsimulator.utility.EnvVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StockService {
    private final WebClient webClient;
    private final String FMP_API_KEY;

    @Autowired
    public StockService(WebClient.Builder webClientBuilder, EnvVariable env) {
        String FMP_API_URL = env.FMP_API_URL();
        this.FMP_API_KEY = env.FMP_API_KEY();

        webClient = webClientBuilder
                .baseUrl(FMP_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    String fetchNews(String symbol, String limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/stock_news")
                        // ----- (1) ----- //
                        .queryParam("tickers", symbol)
                        .queryParam("page", 0)
                        .queryParam("limit", limit)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    String searchStock(String query, String exchange) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/search")
                        .queryParam("query", query)
                        .queryParam("exchange", exchange)
                        .queryParam("limit", 20)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    String fetchFinancialStatement(String symbol, String type, String period, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/" + type + "/" + symbol)
                        .queryParam("period", period)
                        .queryParam("limit", limit)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}


/*
----- (1) -----
Each page of news fetched from the API consists of at least 100 entries (with a
specific symbol). The limit param is useless for pagination since it only limits
number of the entry for the same page. If the first time I fetch 10 news,
the next time I need to fetch 20 news which includes the previous 10 news
that haved been fetched.

I will just fetch the page without using the limit param, and then slice the
array and show 10 entries at a time. when all 100 news are displayed, fetch
the next page from the server

PS: I did not implement the "fetch next page", since 100 news are more than enough,
I don't think someone will scroll all the way down to bottom





* */