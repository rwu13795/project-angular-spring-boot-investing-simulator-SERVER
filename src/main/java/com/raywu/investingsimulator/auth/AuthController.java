package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.auth.dto.SignInRequest;
import com.raywu.investingsimulator.auth.dto.UserInfo;
import com.raywu.investingsimulator.auth.dto.SignUpRequest;
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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final AccountService accountService;
    @Autowired
    private AuthService authService;

    @Autowired
    public AuthController(AccountService accountService, Environment env) {

        this.accountService = accountService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserInfo> signIn(@Valid @RequestBody SignInRequest signInRequest) {

        return authService.signIn(signInRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserInfo> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        return authService.signUp(signUpRequest);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<UserInfo> checkToken(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        int id = Integer.parseInt(request.getAttribute("id").toString());

        return authService.checkAuth(email, id);
    }

    @PutMapping("/account")
    public Account updateUser(@RequestBody Account account) {
        // ---- VERY IMPORTANT ---- //
        // for updating the existing employee, we need to include the ID in the request body
        // (unlike creating a new employee entry, we DO NOT include the ID)
        accountService.save(account);

        return account;
    }

    @DeleteMapping("/account/{id}")
    public String deleteById(@PathVariable int id) {

        // check if the employee which is being deleted exists in the DB
        Account account = accountService.findById(id);

        if(account == null) {
//            throw new TestException("account id not found - " + id);
            throw new RuntimeException("account id not found - " + id);
        }

        accountService.deleteById(id);

        return "account id-" + id + " has been deleted";
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
