package com.example.cardatabase4.web;

import com.example.cardatabase4.Service.JwtService;
import com.example.cardatabase4.domain.AccountCredentials;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        // user 인증
        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
                credentials.username(), credentials.password()
        );
        Authentication auth = authenticationManager.authenticate(creds);

        // 토큰 생성
        String jwts = jwtService.getToken(auth.getName());

        // 생성된 토큰으로 응답을 build()
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }
}
