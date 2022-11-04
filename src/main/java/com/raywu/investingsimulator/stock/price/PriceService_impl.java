package com.raywu.investingsimulator.stock.price;

import com.raywu.investingsimulator.stock.price.dto.PriceHistory;
import com.raywu.investingsimulator.stock.price.dto.PriceHistoryList;
import com.raywu.investingsimulator.stock.price.dto.ShortQuote;
import com.raywu.investingsimulator.utility.EnvVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService_impl implements PriceService{
    private final WebClient webClient;
    private final String FMP_API_KEY;

    @Autowired
    public PriceService_impl(WebClient.Builder webClientBuilder, EnvVariable env) {
        String FMP_API_URL = env.FMP_API_URL();
        this.FMP_API_KEY = env.FMP_API_KEY();

        // increase the WebClient default "maxInMemorySize"
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        webClient = webClientBuilder
                .baseUrl(FMP_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // increase the WebClient default "maxInMemorySize"
                .exchangeStrategies(strategies)
                .build();
    }
    @Override
    public String fetchHistoricalPrice(String from, String to, String time_range, String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/historical-chart/" + time_range + "/" + symbol)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @Override
    public List<PriceHistory> fetchHistoricalPrice_full(String from, String to, String symbol, String time_option) {
        PriceHistoryList list =
         webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/historical-price-full/" + symbol)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(PriceHistoryList.class)
                .block();
        // the fmp-api does not provide the option to skip some data points.
        // the data points for 5-year is way too many, I need to reduce the number of data points
        System.out.println(time_option);
        if(time_option.equals("5Y")) {
            List<PriceHistory> modifiedList = new ArrayList<>();
            for(int i = 0; i < list.getHistorical().size(); i++) {
                if(i % 5 == 0) {
                    modifiedList.add(list.getHistorical().get(i));
                }
            }
            if(list.getHistorical().size() - 1 % 5 != 0) {
                modifiedList.add(list.getHistorical().get(list.getHistorical().size() - 1));
            }
            return modifiedList;
        }
        return list.getHistorical();
    }
    @Override
    public String fetchRealTimeQuote(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/quote/" + symbol)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @Override
    public String fetchShortQuote(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/quote-short/" + symbol)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public ShortQuote[] fetchShortQuoteList(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/quote-short/" + symbol)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(ShortQuote[].class)
                .block();
    }

    @Override
    public String fetchFinancialRatios(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/ratios-ttm/" + symbol)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @Override
    public String fetchPriceChange(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/stock-price-change/" + symbol)
                        .queryParam("apikey", FMP_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
