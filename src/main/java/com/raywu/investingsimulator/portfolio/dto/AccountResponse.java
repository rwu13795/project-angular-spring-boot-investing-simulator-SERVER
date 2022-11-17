package com.raywu.investingsimulator.portfolio.dto;

import com.raywu.investingsimulator.portfolio.account.Account;
import lombok.Data;

@Data
public class AccountResponse {
    private int id;
    private String email;
    private double fund;
    private long joinedAt;

    private double shortSellingDeposit;

    private double totalRealizedGainLoss;

    private double totalRealizedGainLoss_shortSelling;

    private double totalUnrealizedGainLoss;

    private double totalUnrealizedGainLoss_shortSelling;

    public AccountResponse() {}

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.fund = account.getFund();
        this.joinedAt =account.getJoinedAt();
    }
}
