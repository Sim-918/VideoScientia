package com.sim.video;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/popularTV","/register", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("A")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                      // 로그인 폼 경로
                        .loginProcessingUrl("/login")             // POST 로그인 처리 URL
                        .usernameParameter("userId")
                        .passwordParameter("password")
//                        todo 관라자가 로그인 했을 때 /admin 가는게 아니라 홈화면에서 관리자페이지 이동버튼으로 갈수 있게 만들기
//                        .successHandler(customLoginSuccessHandler()) // ✅ 역할에 따라 분기
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/") // 성공 후 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 제거
                        .clearAuthentication(true) // 인증정보 제거
                );

        return http.build();
    }
}
