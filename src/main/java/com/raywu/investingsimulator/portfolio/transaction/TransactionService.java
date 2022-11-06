package com.raywu.investingsimulator.portfolio.transaction;

import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;

import java.util.List;

public interface TransactionService {
    List<Transaction> findByUserIdAndSymbol (int userId, String symbol);

    void saveTransaction(Transaction transaction);

    void saveTransactionShortSell(TransactionShortSell transactionSS);

    Transaction addNewTransaction
            (int userId, double currentPrice, TransactionRequest tr, double realizedGainLoss);

    TransactionShortSell addNewTransactionShortSell
            (int userId, double currentPrice, TransactionRequest tr, double realizedGainLoss);
}
