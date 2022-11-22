package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
import com.raywu.investingsimulator.auth.sendgrid.SendGridService;
import com.raywu.investingsimulator.exception.exceptions.InvalidTokenException;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import com.raywu.investingsimulator.utility.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SendGridService sendGridService;

    @PostMapping("/sign-in")
    public ResponseEntity<UserInfo> signIn(@Valid @RequestBody SignInRequest signInRequest) {

        return authService.signIn(signInRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserInfo> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        return authService.signUp(signUpRequest);
    }

    @GetMapping("/sign-out")
    public ResponseEntity<SignOutResponse> signOut() {

        return authService.signOut();
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<UserInfo> getUserInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        int id = Integer.parseInt(request.getAttribute("id").toString());

        return authService.getUserInfo(email, id);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<CheckAuthResponse> checkAuth() {
        return authService.checkAuth();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return authService.changePassword(request);
    }

    @PostMapping("/reset-password-request")
    public  ResponseEntity<String> generatePasswordResetLink
            (@RequestBody HashMap<String, String> body) throws IOException {
//        sendGridService.sendResetPasswordLink();
        return authService.generatePasswordResetLink(body.get("email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HashMap<String, String>> validatePasswordResetToken
            (@RequestBody HashMap<String, String> body) {

        return authService.validatePasswordResetToken(body.get("token"));
    }

//    @PutMapping("/reset-password")
////    public  ResponseEntity<Void> resetPassword(@Valid @RequestBody String token) {
////
//////        return authService.validatePasswordResetToken(token);
////        return null;
////    }
}
