package com.raywu.investingsimulator.auth.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Token {
    private TokenType tokenType;
    private String tokenString;
    private long duration;
    private LocalDateTime expiryDate;

    public enum TokenType {
        ACCESS, REFRESH, PW_RESET
    }
}
