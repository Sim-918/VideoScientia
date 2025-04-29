package com.sim.video.config;


import com.sim.video.service.UserSecurityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;

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
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final UserSecurityService userSecurityService;

    //패스워드 암호화(해시)하는 메서드
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //아이디,비번을 확인하고 db에서 사용자 정보를 가져와서 비밀번호 검증하는 메서드
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userSecurityService); //로그인할 때 db에서 User불러옴
        provider.setPasswordEncoder(passwordEncoder()); //비밀번호 비교할 때 인코더 등록
        provider.setHideUserNotFoundExceptions(false); //아이디가 존재하지 않는 경우 UsernameNotFoundException을 던짐
        return provider;
    }

    // Spring Security 전체 인증을 총괄하는 메서드
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)     //스프링에서 내부 빌더 가져옴
                .authenticationProvider(daoAuthenticationProvider())    // DaoAuthenticationProvider메서드 등록
                .build();   // AuthenticationManager 객체등록
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf-> csrf.disable()) // fetch 사용 시 csrf 비활성화 (token 처리)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/popularTV","/register", "/login*","/api/check-id", "/api/check-email","/favicon.ico", "/css/**", "/js/**").permitAll() //인증없이 접근 가능 경로
                .anyRequest().authenticated()   //나머지 요청 인증
            )
            .formLogin(form->form
                    .loginPage("/login") // 커스텀 로그인 페이지(비인증 사용자가 인증 필요한 url로 접근할 때 /login자동 리다이렉트)
                    .loginProcessingUrl("/login") //로그인 post url(fetch가 여기 침)
                    .successHandler((request, response, authentication) -> {  //로그인 성공 후 동작을 커스터 마이징하는 코드(성공했을 때 200응답)
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
                    .failureHandler(failureHandler) // 로그인 실패시 이동할 주소
                    .permitAll()
            )
            .logout(logout->logout
                    .logoutUrl("/logout") // 로그아웃 url
                    .logoutSuccessUrl("/") //로그아웃 후 이동할 주소
                    .invalidateHttpSession(true) //세션삭제
                    .deleteCookies("JSESSIONID") // 쿠키 삭제
            )
                .build();
        // 추후 코사인 유사도 캐시,세션
}
}
