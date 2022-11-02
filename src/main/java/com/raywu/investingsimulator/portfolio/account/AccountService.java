package com.raywu.investingsimulator.portfolio.account;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(int id);

    Account findByEmail(String email);

    void save(Account account);

    void deleteById(int id);

//    Account findByLastName(String lastName);
//
//    List<Account> getUsersByPage(int pageNum);
}
