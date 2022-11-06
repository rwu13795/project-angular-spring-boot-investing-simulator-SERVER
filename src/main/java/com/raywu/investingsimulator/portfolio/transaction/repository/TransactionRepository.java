package com.raywu.investingsimulator.portfolio.transaction.repository;

import com.raywu.investingsimulator.portfolio.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM TRANSACTION t WHERE t.user_id = ?1 and t.symbol = ?2",
            nativeQuery = true)
    List<Transaction> findByUserIdAndSymbol(int userId, String symbol);


//    // create a custom getEmployeesByPage method using native SQL
//    @Query(value = "SELECT * FROM EMPLOYEE LIMIT ?1 OFFSET ?2",
//            nativeQuery = true)
//    List<Account> getUsersByPage(int limit, int offset);
//    // Limit = items_per_page
//    // OFFSET = (pageNum - 1) * Limit

}
