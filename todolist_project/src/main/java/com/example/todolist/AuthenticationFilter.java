package com.example.todolist;

import com.example.todolist.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // 생성자 주입
    public AuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청에서 인증된 사용자 이름(username)을 추출 (토큰 검증 포함)
        String username = jwtService.getAuthUser(request);

        // SecurityContext에 이미 인증 정보가 없고, 사용자 이름이 추출된 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 2. UserDetailsService를 사용하여 UserDetails 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 3. 인증 토큰 생성 (권한 정보를 포함하여 생성)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // 비밀번호는 이미 검증되었으므로 null
                    userDetails.getAuthorities() // 사용자의 권한 정보
            );

            // 4. Security Context에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
