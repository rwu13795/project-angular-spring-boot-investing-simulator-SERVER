package com.raywu.investingsimulator.portfolio.watchlist;


import com.raywu.investingsimulator.portfolio.transaction.entity.TransactionShortSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistPK> {

    @Query(value = "SELECT * FROM watchlist w WHERE w.user_id = ?1",
            nativeQuery = true)
    List<Watchlist> getWatchlist(int userId);

    // @Modifying and @Transactional are needed for the update/delete query!
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM watchlist w WHERE w.user_id = ?1 and w.symbol = ?2",
            nativeQuery = true)
    void removeFromList(int userId, String symbol);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO watchlist VALUES(?1, ?2, ?3) ON CONFLICT(user_id, symbol) DO NOTHING",
            nativeQuery = true)
    void addToList(int userId, String symbol, String exchange);

    @Query(value = "SELECT * FROM watchlist w WHERE w.user_id = ?1 LIMIT 10 OFFSET ?2",
            nativeQuery = true)
    List<Watchlist> findByPageNum(int userId, int offset);
}
