package com.raywu.investingsimulator.portfolio;

import org.springframework.http.ResponseEntity;

public interface PortfolioService {
    ResponseEntity<Portfolio> getPortfolio(int user_id);
}
