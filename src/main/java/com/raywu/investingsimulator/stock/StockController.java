package com.raywu.investingsimulator.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "http://localhost:4200")
public class StockController {

    @Autowired
    private StockService_impl stockService;

    @GetMapping("/news")
    public String fetchNews(@RequestParam String symbol, @RequestParam String limit) {

        return stockService.fetchNews(symbol, limit);
    }

    @GetMapping("/search")
    public String searchStock(@RequestParam String query, @RequestParam String exchange) {

        return stockService.searchStock(query, exchange);
    }

    @GetMapping("/financial-statement")
    public String fetchFinancialStatement(@RequestParam String symbol,
                                          @RequestParam String type,
                                          @RequestParam String period,
                                          @RequestParam int limit) {

        return stockService.fetchFinancialStatement(symbol, type, period, limit);
    }

    @GetMapping("/profile")
    public String fetchCompanyProfile(@RequestParam String symbol) {

        return stockService.fetchCompanyProfile(symbol);
    }
}
