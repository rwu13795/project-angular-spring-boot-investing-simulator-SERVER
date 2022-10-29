package com.raywu.investingsimulator.stock.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stock/search")
@CrossOrigin(origins = "http://localhost:4200")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("")
    public String searchStock(@RequestParam String query, @RequestParam String exchange) {

        // Spring will automatically use Jackson to convert the employee object into JSON
        return searchService.searchStock(query, exchange);
    }
}
