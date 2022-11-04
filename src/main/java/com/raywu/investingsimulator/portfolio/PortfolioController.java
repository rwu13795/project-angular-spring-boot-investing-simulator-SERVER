package com.raywu.investingsimulator.portfolio;

import com.raywu.investingsimulator.auth.AuthService;
import com.raywu.investingsimulator.portfolio.account.Account;
import com.raywu.investingsimulator.portfolio.account.AccountService;
import com.raywu.investingsimulator.portfolio.asset.Asset;
import com.raywu.investingsimulator.portfolio.asset.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PortfolioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PortfolioService portfolioService;

    // refresh the access token and refresh token on every request that sent to the "/portfolio" route
    // since the JWTFilter is applied to this route, it is certain that the user email is packed inside
    // the request.getAttribute("email")
    @GetMapping("/refresh-token")
    public ResponseEntity<String> checkToken(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        int id = (int) request.getAttribute("id");

        HttpHeaders headers = authService.refreshTokens(email, id);

        return ResponseEntity.ok().headers(headers).body("token refreshed");
    }

    @GetMapping("/get-portfolio")
    public ResponseEntity<Portfolio> getPortfolio(HttpServletRequest request) {
        int id = Integer.parseInt(request.getAttribute("id").toString());

        return portfolioService.getPortfolio(id);
    }
}
