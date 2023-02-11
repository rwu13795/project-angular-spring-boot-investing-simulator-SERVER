package com.raywu.investingsimulator.stock.preview;

import com.raywu.investingsimulator.stock.preview.dto.PeersList;
import com.raywu.investingsimulator.utility.EnvVariable;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PreviewService_impl implements PreviewService {
    private final WebClient webClient;
    private final String FMP_API_KEY;

    @Autowired
    public PreviewService_impl(WebClient.Builder webClientBuilder, EnvVariable env) {
        String FMP_API_URL = env.FMP_API_URL();
        this.FMP_API_KEY = env.FMP_API_KEY();

        webClient = webClientBuilder
                .baseUrl(FMP_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String fetchPreviewList(String option) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v3/stock_market/" + option)
                    .queryParam("apikey", FMP_API_KEY)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @Override
    public String fetchPeersList(String symbol) {
        PeersList[] list = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                            .path("/v4/stock_peers")
                            .queryParam("symbol", symbol)
                            .queryParam("apikey", FMP_API_KEY)
                            .build())
                        .retrieve()
                        // ----- (1) ----- //
                        .bodyToMono(PeersList[].class)
                        .block();

        // some stocks do not have any peers, so the PeersList[] will be empty
        if(list.length < 1) return null;

        return this.fetchPeersInfo(StringUtils.join(list[0].getPeersList(), ','));
    }

    private String fetchPeersInfo(String symbolList) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v3/quote/" + symbolList)
                    .queryParam("apikey", FMP_API_KEY)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

/*
----- (1) -----
Since the response from the "stock_peer" is

     [
        {
            "symbol": "AAPL",
            "peersList": ["xxx", "xxxx"]
        }
     ]

the "PeersList" is inside an array, I need to use "PeersList[].class" in order to let
Jackson map the object correctly.

*/