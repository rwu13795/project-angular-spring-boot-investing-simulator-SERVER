package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.auth.AuthService;
import com.raywu.investingsimulator.portfolio.dto.TransactionCount;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.TransactionService;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PortfolioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private TransactionService transactionService;

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

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(HttpServletRequest request,
                                                             @RequestParam String symbol) {
        int id = Integer.parseInt(request.getAttribute("id").toString());
        List<Transaction> transactions = transactionService.findByUserIdAndSymbol(id, symbol);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/transactions/by-page")
    public ResponseEntity<List<? extends TransactionTemplate>> getTransactionsByTime
            (HttpServletRequest request, @RequestParam String symbol,
             @RequestParam int pageNum, @RequestParam String type) {

        int id = Integer.parseInt(request.getAttribute("id").toString());
        List<? extends TransactionTemplate> transactions = transactionService.getTransactionsByPage(id, symbol, pageNum, type);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/transactions/count")
    public ResponseEntity<TransactionCount> getTransactionsCount
            (HttpServletRequest request, @RequestParam String symbol, @RequestParam String type) {

        int id = Integer.parseInt(request.getAttribute("id").toString());
        long count = transactionService.getTransactionsCount(id, symbol, type);

        return ResponseEntity.ok().body(new TransactionCount(count));
    }
}
