package com.raywu.investingsimulator.stock.price;

import com.raywu.investingsimulator.stock.price.dto.PriceHistory;
import com.raywu.investingsimulator.stock.price.dto.ShortQuote;

import java.util.List;

public interface PriceService {
    String fetchHistoricalPrice(String from, String to, String time_range, String symbol);

    List<PriceHistory> fetchHistoricalPrice_full(String from, String to, String symbol, String time_option);

    String fetchRealTimeQuote(String symbol);

    String fetchShortQuote(String symbol);

    public ShortQuote[] fetchShortQuoteList(String symbol);

    String fetchFinancialRatios(String symbol);

    String fetchPriceChange(String symbol);
}
