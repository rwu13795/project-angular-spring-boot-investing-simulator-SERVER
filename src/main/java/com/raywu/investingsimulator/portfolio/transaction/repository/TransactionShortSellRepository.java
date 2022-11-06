package com.raywu.investingsimulator.portfolio.transaction.repository;

import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionShortSellRepository extends JpaRepository<TransactionShortSell, Integer> {

    @Query(value = "SELECT * FROM transaction_short_sell t WHERE t.user_id = ?1 and t.symbol = ?2",
            nativeQuery = true)
    List<TransactionShortSell> findByUserIdAndSymbol(int userId, String symbol);

}
