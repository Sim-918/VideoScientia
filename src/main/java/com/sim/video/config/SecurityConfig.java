package com.sim.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/","/popularTV","/register", "/login", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("A")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")                      // 로그인 폼 경로
//                        .loginProcessingUrl("/login")             // POST 로그인 처리 URL
//                        .usernameParameter("userId")
//                        .passwordParameter("password")
////                        todo 관라자가 로그인 했을 때 /admin 가는게 아니라 홈화면에서 관리자페이지 이동버튼으로 갈수 있게 만들기
//                         //todo handller -> 잠겼을 때, 로그인실패 때, 이런것들에 제어
////                        .successHandler(customLoginSuccessHandler()) // ✅ 역할에 따라 분기
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // 로그아웃 URL
//                        .logoutSuccessUrl("/") // 성공 후 이동
//                        .invalidateHttpSession(true) // 세션 무효화
//                        .deleteCookies("JSESSIONID") // 쿠키 제거
//                        .clearAuthentication(true) // 인증정보 제거
//                );
//
//        return http.build();
//    }
//}
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    //패스워드 암호화(해시)하는 메서드
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //현재 폼기반이 아닌 REST API만 사용하기 떄문에 잠깐 꺼둠
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/popularTV","/register", "/login", "/css/**", "/js/**").permitAll() //인증없이 접근 가능 경로
                        .anyRequest().authenticated()   //나머지 요청 인증
                )
                .build();
    }
}
