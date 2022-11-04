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

        return authService.checkAuth(request);
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


    // test the password encryption
    @GetMapping("/account/pw-en/{password}")
    public String encryptPassword(@PathVariable String password) {

        // the pw is "abcde"

        boolean p = new BCryptPasswordEncoder()
                .matches(password, "$2a$10$7Z/93KgV2JruJX3TJcS.UeCejTXHnNPnEb6ECuf7aA07vR/rr7kBG");

        System.out.println("---------- matched? " + p);

        return "ok";
    }


    // test the filtered page
    @GetMapping("/secret-page")
    public String filteredPage(HttpServletRequest request) {

        System.out.println(request.getAttribute("JWT"));

        return "you passed the filter !";
    }

//    @GetMapping("/get-posts-firebase")
//    public String getPostsFirebase() {
//
//        // --- Old method --- //
////		final String url =
////				"https://angular-course-79389-default-rtdb.firebaseio.com/posts.json";
////
////		RestTemplate restTemplate = new RestTemplate();
////		String result = restTemplate.getForObject(url, String.class);
////
////		System.out.println(result);
//
//        // --- use WebClient to make http request --- //
//
//        String result = postApiService.getPosts();
//
//        return result;
//    }
}
