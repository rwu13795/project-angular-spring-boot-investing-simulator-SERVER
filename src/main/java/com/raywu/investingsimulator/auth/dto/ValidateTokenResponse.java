package com.raywu.investingsimulator.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateTokenResponse {
    String timestamp;
    String email;
}
