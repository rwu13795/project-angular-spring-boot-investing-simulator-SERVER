package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
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
import org.springframework.web.bind.annotation.GetMapping;

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
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Account existedAcct = accountService.findByEmail(email);

        if(existedAcct == null) {
            throw new IncorrectEmailException();
        }

        if(!new BCryptPasswordEncoder().matches(password, existedAcct.getPassword())) {
            throw new IncorrectPasswordException();
        }

        // generate JWT after the user is authenticated
        HttpHeaders responseHeaders = addCookiesToHeaders(email, existedAcct.getId(),true);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new UserInfo(existedAcct));
    }

    @Override
    public ResponseEntity<UserInfo> signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct != null) {
            throw new EmailTakenException();
        }
        if(!signUpRequest.passwordsMatched()) throw new PasswordsNotMatchedException();

        System.out.println("email -----" + email);
        System.out.println("password -----" + password);

        // ---- NOTE ---- //
        // You have to set the employee id to 0, to let Hibernate know that we are
        // create a new employee entry (because we use saveOrUpdate in the DAO)
//        Account newAccount = new Account();
//        newAccount.setId(0);
//        newAccount.setEmail(email);
//        newAccount.setFund(100000);
//
//        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
//        newAccount.setPassword(encryptedPassword);
//
//        accountService.save(newAccount);
//
//        // generate JWT
//        HttpHeaders responseHeaders = addCookiesToHeaders(email, newAccount.getId(),true);
//

        return ResponseEntity.ok()
//                .headers(responseHeaders)
                .body(new UserInfo(999,"test@pass.com",1000, "test date"));
    }
    @Override
    public ResponseEntity<SignOutResponse> signOut() {
        HttpHeaders responseHeaders = deleteTokens();
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new SignOutResponse("Signed out"));
    }

    @Override
    public ResponseEntity<UserInfo> getUserInfo(String email, int id) {
        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct == null) {
            throw new InvalidTokenException("Invalid email in the token");
        }
        HttpHeaders headers = refreshTokens(email, id);
        return ResponseEntity.ok()
                .headers(headers)
                .body(new UserInfo(existedAcct));
    }

    @Override
    public ResponseEntity<CheckAuthResponse> checkAuth() {
        return ResponseEntity.ok().body(new CheckAuthResponse(true));
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
        return tokenProvider.getUserInfoFromToken(jwt);
    }
    @Override
    public HttpHeaders refreshTokens(String email, int id) {
        return addCookiesToHeaders(email, id, false);
    }

    public void resetPasswordLink() {

    }

    private HttpHeaders addCookiesToHeaders(String email, int id, boolean newRefresh) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken = tokenProvider.generateAccessToken(email, id);
        addAccessTokenCookie(responseHeaders, newAccessToken);

        // only add the refresh token when user signs in and only update the access token on API request
        if(newRefresh) {
            Token newRefreshToken = tokenProvider.generateRefreshToken(email, id);
            addRefreshTokenCookie(responseHeaders, newRefreshToken);
        }
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

    private HttpHeaders deleteTokens() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieHelper.deleteAccessTokenCookie());
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieHelper.deleteRefreshTokenCookie());
        return httpHeaders;
    }
}
