package com.raywu.investingsimulator.portfolio.transaction;

import com.raywu.investingsimulator.portfolio.dto.TransactionGainLoss;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionTemplate;

import java.util.List;

public interface TransactionService {
    List<Transaction> findByUserIdAndSymbol (int userId, String symbol);

    void saveTransaction(Transaction transaction);

    void saveTransactionShortSell(TransactionShortSell transactionSS);

    Transaction addNewTransaction
            (int userId, double currentPrice,
             TransactionRequest tr, TransactionGainLoss transactionGainLoss);

    TransactionShortSell addNewTransactionShortSell
            (int userId, double currentPrice,
             TransactionRequest tr, TransactionGainLoss transactionGainLoss);

    List<? extends TransactionTemplate> getTransactionsByPage(int userId, String symbol, int pageNum, String type);

    long getTransactionsCount(int userId, String symbol, String type);
}
