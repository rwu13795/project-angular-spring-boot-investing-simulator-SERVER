package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


public interface AuthService {

    ResponseEntity<UserInfo> signIn(SignInRequest signInRequest);

    ResponseEntity<UserInfo> signUp(SignUpRequest signUpRequest);

    ResponseEntity<UserInfo> getUserInfo(String email, int id);

    ResponseEntity<Void> changePassword(ChangePasswordRequest request);

    String validateJWT(HttpServletRequest request);

    HttpHeaders refreshAccessToken(String email, int id);

    ResponseEntity<CheckAuthResponse> checkAuth();

     ResponseEntity<SignOutResponse> signOut();
}
