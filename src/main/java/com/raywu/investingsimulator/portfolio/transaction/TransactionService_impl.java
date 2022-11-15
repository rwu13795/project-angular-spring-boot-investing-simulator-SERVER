package com.raywu.investingsimulator.portfolio.transaction;

import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import com.raywu.investingsimulator.portfolio.transaction.repository.TransactionRepository;
import com.raywu.investingsimulator.portfolio.transaction.repository.TransactionShortSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService_impl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionShortSellRepository transactionShortSellRepository;

    @Override
    public List<Transaction> findByUserIdAndSymbol(int userId, String symbol) {
        return transactionRepository.findByUserIdAndSymbol(userId, symbol);
    }

    @Override
    public void saveTransaction(Transaction transaction) {

        transactionRepository.save(transaction);
    }

    @Override
    public void saveTransactionShortSell(TransactionShortSell transactionSS) {

        transactionShortSellRepository.save(transactionSS);
    }

    @Override
    public Transaction addNewTransaction(int userId, double currentPrice,
                                  TransactionRequest tr, double realizedGainLoss) {

        boolean buy = tr.getType().equals(TransactionType.BUY.name());

        Transaction transaction = new Transaction();
        transaction.setId(0);
        transaction.setUserId(userId);
        transaction.setSymbol(tr.getSymbol());
        transaction.setBuy(buy);
        transaction.setPricePerShare(currentPrice);
        transaction.setShares(tr.getShares());
        transaction.setRealizedGainLoss(realizedGainLoss);

        saveTransaction(transaction);
        return transaction;
    }

    @Override
    public TransactionShortSell addNewTransactionShortSell(int userId, double currentPrice,
                                  TransactionRequest tr, double realizedGainLoss) {

        boolean shortSell = tr.getType().equals(TransactionType.SELL_SHORT.name());

        TransactionShortSell transactionSS = new TransactionShortSell();
        transactionSS.setId(0);
        transactionSS.setUserId(userId);
        transactionSS.setSymbol(tr.getSymbol());
        transactionSS.setShortSell(shortSell);
        transactionSS.setPricePerShare(currentPrice);
        transactionSS.setShares(tr.getShares());
        transactionSS.setRealizedGainLoss(realizedGainLoss);

        saveTransactionShortSell(transactionSS);
        return transactionSS;
    }
}
