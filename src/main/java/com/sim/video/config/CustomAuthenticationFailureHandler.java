package com.sim.video.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler { //Spring Security의 로그인 실패 처리용 인터페이스 구현
    //로그인 실패했을 때 자동으로 호출해주는 메서드
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    //HTTP 응답 상태 코드 401로 설정
        response.setContentType("application/json;charset=UTF-8"); //응답 타입 json으로 설정하고 utf-8로 인코딩

        String message = "아이디 또는 비밀번호를 확인해주세요.";

        if (exception instanceof UsernameNotFoundException) {
            message = "존재하지 않는 아이디입니다.";
        } else if (exception instanceof BadCredentialsException) {
            message = "비밀번호를 확인해주세요.";
        }

        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
