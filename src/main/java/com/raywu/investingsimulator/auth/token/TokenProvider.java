package com.raywu.investingsimulator.auth.token;

import com.raywu.investingsimulator.auth.dto.UserInfo;

import java.time.LocalDateTime;

public interface TokenProvider {
    Token generateAccessToken(String email, int id);

    Token generateRefreshToken(String email, int id);

    String getUserInfoFromToken(String token);

//    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
