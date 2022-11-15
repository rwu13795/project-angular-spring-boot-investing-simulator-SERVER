package com.raywu.investingsimulator.stock.stock;

import com.raywu.investingsimulator.stock.stock.dto.SearchResult;

public interface StockService {
    String fetchNews(String symbol, String limit);
    SearchResult[] searchStock(String query);
    String fetchFinancialStatement(String symbol, String type, String period, int limit);
    String fetchCompanyProfile(String symbol);
}
