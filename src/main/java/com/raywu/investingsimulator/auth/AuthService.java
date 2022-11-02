package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.SignInRequest;
import com.raywu.investingsimulator.auth.dto.SignInResponse;
import com.raywu.investingsimulator.auth.dto.SignUpRequest;
import com.raywu.investingsimulator.portfolio.account.Account;
import org.springframework.http.ResponseEntity;


public interface AuthService {

    ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest);

    ResponseEntity<SignInResponse> signUp(SignUpRequest signUpRequest);
}
