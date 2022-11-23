package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {
    @Autowired
    private AuthService authService;

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
    public  ResponseEntity<Void> generatePasswordResetLink
            (@RequestBody HashMap<String, String> body) throws IOException {

        return authService.generatePasswordResetLink(body.get("email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ValidateTokenResponse> validatePasswordResetToken
            (@RequestBody HashMap<String, String> body) {

        return authService.validatePasswordResetToken(body.get("token"));
    }

    @PutMapping("/reset-password")
    public  ResponseEntity<Void> resetPassword(@Valid @RequestBody SignUpRequest body) {

        return authService.resetPassword(body);
    }
}
