package com.example.todolist;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT 토큰이 없거나 유효하지 않아 인증에 실패했을 때 처리하는 클래스
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // 401 Unauthorized 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // 오류 메시지를 JSON 형식으로 응답
        PrintWriter writer = response.getWriter();
        String jsonError = String.format("{\"error\": \"%s\", \"message\": \"%s\"}",
                "Unauthorized",
                "유효한 JWT 토큰이 필요합니다.");
        writer.write(jsonError);
        writer.flush();
    }
}
