package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PortfolioController {

    @Autowired
    private AuthService authService;

    // refresh the access token and refresh token on every request that sent to the "/portfolio" route
    // since the JWTFilter is applied to this route, it is certain that the user email is packed inside
    // the request.getAttribute("email")
    @GetMapping("/refresh-token")
    public ResponseEntity<String> checkToken(HttpServletRequest request) {

        HttpHeaders headers = authService.refreshTokens((String) request.getAttribute("email"));

        return ResponseEntity.ok().headers(headers).body("token refreshed");
    }

    @GetMapping("/get-portfolio")
    public ResponseEntity<HashMap<String, String>> getPortfolio(HttpServletRequest request) {

        HttpHeaders headers = authService.refreshTokens((String) request.getAttribute("email"));

        HashMap<String,String> res = new HashMap<>();
        res.put("res", "testing portfolio");

        return ResponseEntity.ok().headers(headers).body(res);
    }
}
