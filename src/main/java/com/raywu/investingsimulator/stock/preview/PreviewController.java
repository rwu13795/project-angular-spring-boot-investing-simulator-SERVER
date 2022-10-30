package com.raywu.investingsimulator.stock.preview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stock/preview")
@CrossOrigin(origins = "http://localhost:4200")
public class PreviewController {
    @Autowired
    private PreviewService previewService;

    @GetMapping("/all/{option}")
    public String fetchPreviewList(@PathVariable String option) {

        return previewService.fetchPreviewList(option);
    }

    @GetMapping("/peers")
    public String fetchPeersList(@RequestParam String symbol) {

        return previewService.fetchPeersList(symbol);
    }
}
