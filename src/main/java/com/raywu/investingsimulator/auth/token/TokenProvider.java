package com.raywu.investingsimulator.auth.token;

import com.raywu.investingsimulator.auth.dto.UserInfo;

import java.time.LocalDateTime;

public interface TokenProvider {
    Token generateAccessToken(String email);

    Token generateRefreshToken(String email);

    String getUserEmailFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
