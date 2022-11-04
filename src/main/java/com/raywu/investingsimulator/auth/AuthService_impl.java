package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.SignInRequest;
import com.raywu.investingsimulator.auth.dto.UserInfo;
import com.raywu.investingsimulator.auth.dto.SignUpRequest;
import com.raywu.investingsimulator.auth.token.Token;
import com.raywu.investingsimulator.auth.token.TokenProvider;
import com.raywu.investingsimulator.exception.exceptions.*;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import com.raywu.investingsimulator.utility.CookieHelper;
import com.raywu.investingsimulator.utility.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class AuthService_impl implements AuthService{
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CookieHelper cookieHelper;


    @Override
    public ResponseEntity<UserInfo> signIn(SignInRequest signInRequest) {
        Account existedAcct = accountService.findByEmail(signInRequest.getEmail());

        if(existedAcct == null) {
            throw new IncorrectEmailException();
        }

        if(!new BCryptPasswordEncoder().matches(signInRequest.getPassword(), existedAcct.getPassword())) {
            throw new IncorrectPasswordException();
        }

        // generate JWT after the user is authenticated
        HttpHeaders responseHeaders = addCookiesToHeaders(signInRequest.getEmail());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new UserInfo(existedAcct.getId(),existedAcct.getEmail(),existedAcct.getFund()));
    }

    @Override
    public ResponseEntity<UserInfo> signUp(SignUpRequest signUpRequest) {
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

        // generate JWT
        HttpHeaders responseHeaders = addCookiesToHeaders(signUpRequest.getEmail());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new UserInfo(newAccount.getId(),newAccount.getEmail(),newAccount.getFund()));
    }

    public ResponseEntity<UserInfo> checkAuth(HttpServletRequest request) {
        Account existedAcct = accountService.findByEmail((String)request.getAttribute("email"));

        if(existedAcct == null) {
            throw new InvalidTokenException("Invalid email in the token");
        }

        return ResponseEntity.ok()
                .body(new UserInfo(existedAcct.getId(),existedAcct.getEmail(),existedAcct.getFund()));
    }

    @Override
    public String validateJWT(HttpServletRequest request){
        // extract the token from the "Cookie"
        if(request.getCookies() == null || request.getCookies().length < 1) {
            throw new InvalidTokenException("No cookies found");
        }
        String accessToken = "";
        String refreshToken = "";
        String jwt = "";
        String jwt_2;
        // the access token is expired, only the refresh token will be inside the cookies
        if(request.getCookies().length == 1) {
            refreshToken = request.getCookies()[0].getValue();
            jwt = SecurityCipher.decrypt(refreshToken);
            if(!tokenProvider.validateToken(jwt)) {
                throw new InvalidTokenException("Invalid refresh token");
            }
        }
        // if both tokens exist in the cookies
        if(request.getCookies().length == 2) {
            for(Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken"))  {
                    accessToken = cookie.getValue();
                }
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
            jwt = SecurityCipher.decrypt(accessToken);
            jwt_2 = SecurityCipher.decrypt(refreshToken);
            if(!tokenProvider.validateToken(jwt) || !tokenProvider.validateToken(jwt_2) ) {
                throw new InvalidTokenException("Invalid token");
            }
        }
        return tokenProvider.getUserEmailFromToken(jwt);
    }

    public HttpHeaders refreshTokens(String email) {
        return addCookiesToHeaders(email);
    }

    private HttpHeaders addCookiesToHeaders(String email) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken = tokenProvider.generateAccessToken(email);
        Token newRefreshToken = tokenProvider.generateRefreshToken(email);
        addAccessTokenCookie(responseHeaders, newAccessToken);
        addRefreshTokenCookie(responseHeaders, newRefreshToken);

        return responseHeaders;
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {

        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieHelper.createAccessTokenCookie(token.getTokenString(), token.getDuration()));
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {

        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieHelper.createRefreshTokenCookie(token.getTokenString(), token.getDuration()));
    }
}
