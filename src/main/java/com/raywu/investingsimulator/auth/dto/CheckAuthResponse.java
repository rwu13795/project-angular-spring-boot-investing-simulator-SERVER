package com.raywu.investingsimulator.auth.dto;

import lombok.Data;

@Data
public class CheckAuthResponse {
    private boolean hasAuth;

    public CheckAuthResponse(boolean hasAuth) {
        this.hasAuth = hasAuth;
    }
}
