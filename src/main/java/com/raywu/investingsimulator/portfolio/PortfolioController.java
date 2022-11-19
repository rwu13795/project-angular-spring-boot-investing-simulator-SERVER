package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.auth.AuthService;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PortfolioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/")
    public ResponseEntity<Portfolio> getPortfolio(HttpServletRequest request) {
        int id = Integer.parseInt(request.getAttribute("id").toString());

        return ResponseEntity.ok().body(portfolioService.getPortfolio(id));
    }

    @PostMapping("/buy-sell")
    public ResponseEntity<Transaction> buyAndSell
            (HttpServletRequest request, @RequestBody TransactionRequest tr) {

        int id = Integer.parseInt(request.getAttribute("id").toString());

        Transaction newTransaction = portfolioService.buyAndSell(id, tr);

        return ResponseEntity.ok().body(newTransaction);
    }

    @PostMapping("/sell-short-buy-to-cover")
    public ResponseEntity<TransactionShortSell> sellShortAndBuyToCover
            (HttpServletRequest request, @RequestBody TransactionRequest tr) {

        int id = Integer.parseInt(request.getAttribute("id").toString());

        TransactionShortSell newTransaction = portfolioService.shortSellAndBuyToCover(id, tr);

        return ResponseEntity.ok().body(newTransaction);
    }
}
