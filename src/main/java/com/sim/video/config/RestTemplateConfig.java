package com.sim.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


// 3.X 버전이면 빈 등록을 명시적으로 요구하는 보안 정책이 적용된 프로젝트로 만들어야함!
//ResTemplate을 알아서 관리하기 위해 Bean으로 등록
//서비스에서 주입 받아 쓸 수 있게 하는 클래스
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
