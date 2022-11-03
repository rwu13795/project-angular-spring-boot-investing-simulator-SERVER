package com.raywu.investingsimulator.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private int id;
    private String email;
    private double fund;
}
