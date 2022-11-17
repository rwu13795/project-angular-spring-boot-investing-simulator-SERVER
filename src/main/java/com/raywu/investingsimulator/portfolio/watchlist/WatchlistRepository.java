package com.raywu.investingsimulator.portfolio.watchlist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistPK> {

    @Query(value = "SELECT * FROM watchlist w WHERE w.user_id = ?1",
            nativeQuery = true)
    List<Watchlist> getWatchlist(int userId);
}
