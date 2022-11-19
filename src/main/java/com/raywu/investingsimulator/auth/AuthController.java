package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.*;
import com.raywu.investingsimulator.auth.sendgrid.SendGridService;
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

    @PutMapping("/account")
    public Account updateUser(@RequestBody Account account) {
        // ---- VERY IMPORTANT ---- //
        // for updating the existing employee, we need to include the ID in the request body
        // (unlike creating a new employee entry, we DO NOT include the ID)
        accountService.save(account);

        return account;
    }

    @GetMapping("/reset-password-link")
    public void sendResetPasswordLink() throws IOException {

        sendGridService.sendResetPasswordLink();
    }

//    // test the custom employeeRepository findByEmail query
//    @GetMapping("/users/last-name/{lastName}")
//    public Account findByLastName(@PathVariable String lastName) {
//
//        // NOTE - the last name is case sensitive
//        return userService.findByLastName(lastName);
//    }
//
//
//    // test the custom employeeRepository getUserByPage query
//    @GetMapping("/users/page/{pageNum}")
//    public List<Account> getUsersByPage(@PathVariable int pageNum) {
//
//        // NOTE - the last name is case sensitive
//        return userService.getUsersByPage(pageNum);
//    }






}
