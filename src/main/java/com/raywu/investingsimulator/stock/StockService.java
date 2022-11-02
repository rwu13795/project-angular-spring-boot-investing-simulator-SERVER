package com.raywu.investingsimulator.stock;

public interface StockService {
    String fetchNews(String symbol, String limit);
    String searchStock(String query, String exchange);
    String fetchFinancialStatement(String symbol, String type, String period, int limit);
    String fetchCompanyProfile(String symbol);
}
