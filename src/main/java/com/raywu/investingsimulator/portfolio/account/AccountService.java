package com.raywu.investingsimulator.portfolio.account;

import com.raywu.investingsimulator.portfolio.dto.TransactionRequest;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(int id);

    Account findByEmail(String email);

    void save(Account account);

    void deleteById(int id);

    void updateAccountFund
            (int id, double currentPrice, TransactionRequest tr, double realizedGainLoss);

}
