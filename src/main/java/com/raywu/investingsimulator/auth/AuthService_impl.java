package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
import com.raywu.investingsimulator.auth.sendgrid.SendGridService;
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
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AuthService_impl implements AuthService{
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private SendGridService sendGridService;


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
        // ---- NOTE ---- //
        // You have to set the employee id to 0, to let Hibernate know that we are
        // create a new employee entry (because we use saveOrUpdate in the DAO)
        Account newAccount = new Account();
        newAccount.setId(0);
        newAccount.setEmail(email);
        newAccount.setFund(200000);
        newAccount.setJoinedAt(System.currentTimeMillis());
        newAccount.setCanResetPassword(false);

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        newAccount.setPassword(encryptedPassword);

        accountService.save(newAccount);

        // generate JWT
        HttpHeaders responseHeaders = addCookiesToHeaders(email, newAccount.getId(),true);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new UserInfo(newAccount));
    }
    @Override
    public ResponseEntity<SignOutResponse> signOut() {
        HttpHeaders responseHeaders = deleteTokens();
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new SignOutResponse("Signed out"));
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest request) {
        String email = request.getEmail();
        String oldPassword = request.getPassword();
        String newPassword = request.getNew_password();

        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct == null) throw new BadRequestException("Invalid email from an authenticated user!?", "email");

        if(!new BCryptPasswordEncoder().matches(oldPassword, existedAcct.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if(!request.passwordsMatched()) throw new PasswordsNotMatchedException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);

        existedAcct.setPassword(encryptedPassword);

        accountService.save(existedAcct);

        return ResponseEntity.ok().body(null);
    }

    @Override
    public ResponseEntity<UserInfo> getUserInfo(String email, int id) {
        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct == null) {
            throw new InvalidTokenException("Invalid email in the token");
        }
        HttpHeaders headers = refreshAccessToken(email, id);
        return ResponseEntity.ok()
                .headers(headers)
                .body(new UserInfo(existedAcct));
    }

    @Override
    public ResponseEntity<CheckAuthResponse> checkAuth() {
        return ResponseEntity.ok().body(new CheckAuthResponse(true));
    }

    @Override
    public ResponseEntity<Void> generatePasswordResetLink(String email) throws IOException {
        if(email == null) throw new IncorrectEmailException();

        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct == null) throw new IncorrectEmailException();
        existedAcct.setCanResetPassword(true);
        accountService.save(existedAcct);

        Token token = tokenProvider.generateToken(email, existedAcct.getId(), Token.TokenType.PW_RESET);
        String encryptedToken = SecurityCipher.encrypt(token.getTokenString());

        sendGridService.sendResetPasswordLink(email, encryptedToken);

        return ResponseEntity.ok().body(null);
    }

    @Override
    public ResponseEntity<ValidateTokenResponse> validatePasswordResetToken(String token) {
        if(token == null) throw new InvalidTokenException("No reset token found");

        String jwt = SecurityCipher.decrypt(token);

        if(!tokenProvider.validateToken(jwt)) throw new InvalidTokenException("Token is expired or invalid");

        String email = tokenProvider.getUserInfoFromToken(jwt).split("_")[0];
        // check if the user has used this link to reset the password before
        Account account = accountService.findByEmail(email);
        if(account == null) throw new BadRequestException("Invalid email from an valid token!?", "email");
        if(!account.isCanResetPassword()) throw new InvalidTokenException("Token has been used");

        LocalDateTime timestamp = tokenProvider.getExpiryDateFromToken(jwt);

        return ResponseEntity.ok().body(new ValidateTokenResponse(timestamp.toString(), email));
    }

    @Override
    public ResponseEntity<Void> resetPassword(SignUpRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Account existedAcct = accountService.findByEmail(email);
        if(existedAcct == null) throw new BadRequestException("Invalid email from an valid token!?", "email");

        if(!request.passwordsMatched()) throw new PasswordsNotMatchedException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        existedAcct.setPassword(encryptedPassword);
        existedAcct.setCanResetPassword(false);
        accountService.save(existedAcct);

        return ResponseEntity.ok().body(null);
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
    public HttpHeaders refreshAccessToken(String email, int id) {

        return addCookiesToHeaders(email, id, false);
    }

    private HttpHeaders addCookiesToHeaders(String email, int id, boolean newRefresh) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Token newAccessToken = tokenProvider.generateToken(email, id, Token.TokenType.ACCESS);
        addAccessTokenCookie(responseHeaders, newAccessToken);

        // only add the refresh token when user signs in and only update the access token on API request
        if(newRefresh) {
            Token newRefreshToken = tokenProvider.generateToken(email, id, Token.TokenType.REFRESH);
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
