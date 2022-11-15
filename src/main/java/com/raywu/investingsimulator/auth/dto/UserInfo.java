package com.raywu.investingsimulator.auth.dto;

import com.raywu.investingsimulator.portfolio.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private int id;
    private String email;
    private double fund;
    private String joinedAt;

    public UserInfo(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.fund = account.getFund();
        this.joinedAt =account.getJoinedAt();
    }
}
