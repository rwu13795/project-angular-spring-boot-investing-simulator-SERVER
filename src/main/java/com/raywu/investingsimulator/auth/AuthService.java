package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.SignInRequest;
import com.raywu.investingsimulator.auth.dto.UserInfo;
import com.raywu.investingsimulator.auth.dto.SignUpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


public interface AuthService {

    ResponseEntity<UserInfo> signIn(SignInRequest signInRequest);

    ResponseEntity<UserInfo> signUp(SignUpRequest signUpRequest);

    ResponseEntity<UserInfo> checkAuth(String email, int id);

    String validateJWT(HttpServletRequest request);

    HttpHeaders refreshTokens(String email, int id);
}
