package com.raywu.investingsimulator.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class SignInRequest {

    @Email(message="Invalid email")
    @NotBlank(message="Required field")
    private String email;

    @Size(min=8, max=20, message="Password must be between 8 and 20 characters in length")
    private String password;
}
