package com.raywu.investingsimulator.stock.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/news")
@CrossOrigin(origins = "http://localhost:4200")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("")
    public String fetchNews(@RequestParam String symbol, @RequestParam String limit) {

        // Spring will automatically use Jackson to convert the employee object into JSON
        return newsService.fetchNews(symbol, limit);
    }
}
