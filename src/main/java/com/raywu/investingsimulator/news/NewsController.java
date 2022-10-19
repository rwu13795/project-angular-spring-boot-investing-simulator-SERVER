package com.raywu.investingsimulator.news;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:4200")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/fetch-news")
    public String fetchNews(@RequestParam String symbol) {

        // Spring will automatically use Jackson to convert the employee object into JSON
        return newsService.fetchNews(symbol);
    }
}
