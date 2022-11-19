package com.raywu.investingsimulator.portfolio.transaction;

import com.raywu.investingsimulator.portfolio.dto.TransactionCount;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio/transaction")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Transaction>> getTransactions(HttpServletRequest request,
                                                             @RequestParam String symbol) {
        int id = Integer.parseInt(request.getAttribute("id").toString());
        List<Transaction> transactions = transactionService.findByUserIdAndSymbol(id, symbol);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/by-page")
    public ResponseEntity<List<? extends TransactionTemplate>> getTransactionsByTime
            (HttpServletRequest request, @RequestParam String symbol,
             @RequestParam int pageNum, @RequestParam String type) {

        int id = Integer.parseInt(request.getAttribute("id").toString());
        List<? extends TransactionTemplate> transactions =
                transactionService.getTransactionsByPage(id, symbol, pageNum, type);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/count")
    public ResponseEntity<TransactionCount> getTransactionsCount
            (HttpServletRequest request, @RequestParam String symbol, @RequestParam String type) {

        int id = Integer.parseInt(request.getAttribute("id").toString());
        long count = transactionService.getTransactionsCount(id, symbol, type);

        return ResponseEntity.ok().body(new TransactionCount(count));
    }
}
