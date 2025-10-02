package com.example.todolist.web;

import com.example.todolist.domain.AppUser;
import com.example.todolist.domain.AppUserRepository;
import com.example.todolist.dto.AccountCredentialsRecord;
import com.example.todolist.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 사용자 인증 및 JWT 토큰 발급
     */
    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentialsRecord credentials) {

        // 1. AuthenticationManager를 사용하여 사용자 인증 시도
        UsernamePasswordAuthenticationToken creds =
                new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());

        Authentication auth = authenticationManager.authenticate(creds);

        // 2. 인증 성공 시 JWT 토큰 생성
        String jwts = jwtService.getToken(auth.getName());

        // 3. 응답 헤더에 토큰 포함하여 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }

    /**
     * 새로운 사용자 회원 가입 (테스트용)
     */
    @PostMapping("/api/users/signup")
    public ResponseEntity<String> signup(@RequestBody AccountCredentialsRecord credentials) {
        if (userRepository.findByUsername(credentials.username()).isPresent()) {
            return new ResponseEntity<>("이미 존재하는 사용자 이름입니다.", HttpStatus.BAD_REQUEST);
        }

        AppUser newUser = new AppUser(
                credentials.username(),
                passwordEncoder.encode(credentials.password()),
                "USER"
        );

        userRepository.save(newUser);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }
}
