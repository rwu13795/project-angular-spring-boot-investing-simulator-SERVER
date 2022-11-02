package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.SignInRequest;
import com.raywu.investingsimulator.auth.dto.SignInResponse;
import com.raywu.investingsimulator.auth.dto.SignUpRequest;
import com.raywu.investingsimulator.exception.exceptions.*;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService_impl implements AuthService{
    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest) {
        Account existedAcct = accountService.findByEmail(signInRequest.getEmail());

        if(existedAcct == null) {
            throw new IncorrectEmailException();
        }

        if(!new BCryptPasswordEncoder().matches(signInRequest.getPassword(), existedAcct.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return ResponseEntity.ok()
                .body(new SignInResponse(existedAcct.getId(),existedAcct.getEmail(),existedAcct.getFund()));
    }

    @Override
    public ResponseEntity<SignInResponse> signUp(SignUpRequest signUpRequest) {
        Account existedAcct = accountService.findByEmail(signUpRequest.getEmail());
        if(existedAcct != null) {
            throw new EmailTakenException();
        }
        if(!signUpRequest.passwordsMatched()) throw new PasswordsNotMatchedException();

        // ---- NOTE ---- //
        // You have to set the employee id to 0, to let Hibernate know that we are
        // create a new employee entry (because we use saveOrUpdate in the DAO)
        Account newAccount = new Account();
        newAccount.setId(0);
        newAccount.setEmail(signUpRequest.getEmail());
        newAccount.setFund(9999.53499);

        String encryptedPassword = new BCryptPasswordEncoder().encode(signUpRequest.getPassword());
        newAccount.setPassword(encryptedPassword);

        accountService.save(newAccount);

        return ResponseEntity.ok()
                .body(new SignInResponse(newAccount.getId(),newAccount.getEmail(),newAccount.getFund()));
    }
}
