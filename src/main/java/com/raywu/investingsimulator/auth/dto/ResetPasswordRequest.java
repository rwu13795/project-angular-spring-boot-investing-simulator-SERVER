package com.raywu.investingsimulator.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
    @Size(min=8, max=20, message="Password must be between 8 and 20 characters in length")
    private String password;
    @Size(min=8, max=20, message="Password must be between 8 and 20 characters in length")
    private String confirm_password;

    public boolean passwordsMatched() {
        return password.equals(confirm_password);
    }
}
