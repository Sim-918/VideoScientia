package com.sim.video.controller;

import com.sim.video.dto.LoginRequestDto;
import com.sim.video.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @GetMapping
    public String loginPage(){
        return "login";
    }


    //이제 이코드는 필요없다. UserDetailsService 기반에 자동 로그인을 구현했고 /login은 Security알아서 처리하게 만들었다.
//    @PostMapping
//    @ResponseBody
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto){
//        try{
//            userService.login(requestDto);
//            return ResponseEntity.ok().build();
//        } catch (IllegalArgumentException e){
//            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
//
//        }
//    }
//    private record ErrorResponse(String message){}
}