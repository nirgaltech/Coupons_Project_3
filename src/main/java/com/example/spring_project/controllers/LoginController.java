package com.example.spring_project.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.spring_project.beans.Company;
import com.example.spring_project.exception.ExceptionCoupons;
import com.example.spring_project.facade.ClientFacade;
import com.example.spring_project.facade.CompanyFacade;
import com.example.spring_project.login.ClientType;
import com.example.spring_project.login.LoginManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private LoginManager loginManager;
    private HashMap<String, ClientFacade> tokensStore;

    // Dependency Injection
    public LoginController(LoginManager loginManager, HashMap<String, ClientFacade> tokensStore) {
        this.loginManager = loginManager;
        this.tokensStore = tokensStore;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(String email, String password, ClientType clientType) throws ExceptionCoupons {
       ClientFacade facade = loginManager.login(email, password, clientType);
       if(facade != null){
           // login successful
           String token = createToken(facade);
           // save token in token store...
           tokensStore.put(token, facade);
           return ResponseEntity.ok(token);
       } else {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
       }
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request){
        // remove the token (and facade) from the HashMap!!!
        tokensStore.remove(request.getHeader("Authorization").replace("Bearer ", ""));
        return "yeepy!";
    }

    @ExceptionHandler(ExceptionCoupons.class)
    public ResponseEntity<String> handleLoginException(ExceptionCoupons ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    private String createToken(ClientFacade facade) throws ExceptionCoupons {
        String token = "";
        if(facade instanceof CompanyFacade) {
            Company comp = ((CompanyFacade) facade).getCompanyDetails();
            Instant expires = Instant.now().plus(30, ChronoUnit.MINUTES);
            token = JWT.create()
                    .withClaim("id", comp.getId())
                    .withClaim("name", comp.getName())
                    .withClaim("email", comp.getEmail())
                    .withClaim("role", ClientType.Company.toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(expires)
                    .sign(Algorithm.none());
        } /*else if(facade instanceof AdminFacade){
            Instant expires = Instant.now().plus(30, ChronoUnit.MINUTES);
            token = JWT.create()
                    .withClaim("name", "Admin")
                    .withClaim("role", "administrator")
                    .withExpiresAt(expires)
                    .sign(Algorithm.none());
        } else */

        return token;
    }
}
