package com.raywu.investingsimulator.auth.token;

import java.time.LocalDateTime;

public interface TokenProvider {
    Token generateToken(String email, int id, Token.TokenType tokenType);

    String getUserInfoFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
