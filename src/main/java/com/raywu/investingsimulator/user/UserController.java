package com.raywu.investingsimulator.user;

import com.raywu.investingsimulator.user.User;
import com.raywu.investingsimulator.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // inject employee service
    private final UserService userService;
    private final Environment env;
    private final String FMP_API_KEY;

    @Autowired
    public UserController(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
        // the local env variables are set inside the intellij
        // run -> edit configuration
        this.FMP_API_KEY = this.env.getProperty("FMP_API_KEY");
    }

    @GetMapping("/users")
    public List<User> findAll() {

        // Spring will automatically use Jackson to convert the employee object into JSON
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable int id) {

        return userService.findById(id);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User employee) {

        // ---- VERY IMPORTANT ---- //
        // You have to set the employee id to 0, to let Hibernate know that we are
        // create a new employee entry (because we use saveOrUpdate in the DAO)
        employee.setId(0);

        userService.save(employee);

        return employee;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User employee) {

        // ---- VERY IMPORTANT ---- //
        // for updating the existing employee, we need to include the ID in the request body
        // (unlike creating a new employee entry, we DO NOT include the ID)
        userService.save(employee);

        return employee;
    }

    @DeleteMapping("/users/{id}")
    public String deleteById(@PathVariable int id) {

        // check if the employee which is being deleted exists in the DB
        User employee = userService.findById(id);

        if(employee == null) {
//            throw new TestException("User id not found - " + id);
            throw new RuntimeException("User id not found - " + id);
        }

        userService.deleteById(id);

        return "User id-" + id + " has been deleted";
    }


    // test the custom employeeRepository findByEmail query
    @GetMapping("/users/last-name/{lastName}")
    public User findByLastName(@PathVariable String lastName) {

        // NOTE - the last name is case sensitive
        return userService.findByLastName(lastName);
    }


    // test the custom employeeRepository getUserByPage query
    @GetMapping("/users/page/{pageNum}")
    public List<User> getUsersByPage(@PathVariable int pageNum) {

        // NOTE - the last name is case sensitive
        return userService.getUsersByPage(pageNum);
    }


    // test the password encryption
    @GetMapping("/users/pw-en/{password}")
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
