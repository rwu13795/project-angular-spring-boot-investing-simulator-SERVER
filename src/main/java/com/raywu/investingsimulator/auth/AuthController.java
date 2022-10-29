package com.raywu.investingsimulator.auth;

import com.raywu.investingsimulator.exception.NotFoundException;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    // inject employee service
    private final AccountService accountService;
    private final Environment env;
    private final String FMP_API_KEY;

    @Autowired
    public AuthController(AccountService accountService, Environment env) {
        this.accountService = accountService;
        this.env = env;
        // the local env variables are set inside the intellij
        // run -> edit configuration
        this.FMP_API_KEY = this.env.getProperty("FMP_API_KEY");
    }

    @GetMapping("/account")
    public List<Account> findAll() {

        // Spring will automatically use Jackson to convert the employee object into JSON
        return accountService.findAll();
    }

    @GetMapping("/account/{id}")
    public Account findById(@PathVariable int id) {

        return accountService.findById(id);
    }

    @GetMapping("/account/email")
    public Account findById(@RequestParam(required = false) String email) {

        Account account = accountService.findByEmail(email);

        if(account == null) {
           throw new NotFoundException("No account is found with email - " + email);
        }
        return account;
    }

    @PostMapping("/sign-in")
    @ResponseBody
    public Account signIn(@RequestBody Account account) {
        Account existedAcct = accountService.findByEmail(account.getEmail());
        if(existedAcct == null) {
            throw new NotFoundException("No account is found with email - " + account.getEmail());
        }

//        String encryptedPassword = new BCryptPasswordEncoder().encode(account.getPassword());
//
//
//
//        System.out.println("PW -----"+ account.getPassword());
//        System.out.println("encrypted PW -----"+ encryptedPassword);
//        System.out.println("existedAcct PW -----"+ existedAcct.getPassword());

        if(!new BCryptPasswordEncoder().matches(account.getPassword(), existedAcct.getPassword())) {
            throw new NotFoundException("Incorrect password");
        }
        existedAcct.setPassword("xxxxxxxxxxxx");
        return existedAcct;
    }

    @PostMapping("/account/new")
    /*The @ResponseBody annotation tells a controller that the object returned
      is automatically serialized into JSON and passed back into the HttpResponse
      object. */
    @ResponseBody
    public Account addUser(@RequestBody Account account) {
        System.out.println("-------in post new account---------");
        // ---- VERY IMPORTANT ---- //
        // You have to set the employee id to 0, to let Hibernate know that we are
        // create a new employee entry (because we use saveOrUpdate in the DAO)
        account.setId(0);
        account.setFund(9999.9999);

        String encryptedPassword = new BCryptPasswordEncoder().encode(account.getPassword());
        account.setPassword(encryptedPassword);
        System.out.println("---------- encryptedPassword " + encryptedPassword);

        accountService.save(account);

        return account;
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
    public String filteredPage() {

        System.out.println("API_KEY " + FMP_API_KEY);

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
