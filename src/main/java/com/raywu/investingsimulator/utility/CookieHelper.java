package com.raywu.investingsimulator.utility;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieHelper {

    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";

    public String createAccessTokenCookie(String token, long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from(accessToken, encryptedToken)
                // the "maxAge" is in seconds NOT millisecond
                .maxAge(duration / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite(".trading-simulator.live")
                .domain(".trading-simulator.live")
                .path("/")
                .build()
                .toString();
    }

    public String createRefreshTokenCookie(String token, long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from(refreshToken, encryptedToken)
                .maxAge(duration / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite(".trading-simulator.live")
                .domain(".trading-simulator.live")
                .path("/")
                .build()
                .toString();
    }

    public String deleteAccessTokenCookie() {
        return ResponseCookie.from(accessToken, "deleted")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite(".trading-simulator.live")
                .domain(".trading-simulator.live")
                .path("/")
                .build()
                .toString();
    }

    public String deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshToken, "deleted")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite(".trading-simulator.live")
                .domain(".trading-simulator.live")
                .path("/")
                .build()
                .toString();
    }
}


/*
dev

      return ResponseCookie.from(refreshToken, encryptedToken)
                .maxAge(duration / 1000)
                .httpOnly(true)
                .secure(true)
                .domain("localhost")
                .path("/")
                .build()
                .toString();



* */
