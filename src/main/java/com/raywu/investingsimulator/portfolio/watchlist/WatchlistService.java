package com.raywu.investingsimulator.portfolio.watchlist;

import java.util.HashMap;
import java.util.List;

public interface WatchlistService {
    HashMap<String, String> getWatchlist(int userId);
}
