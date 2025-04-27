package com.sim.video.controller;

import com.sim.video.dto.RegisterRequestDto;
import com.sim.video.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @GetMapping
    public String registerPage(){
        return "register";
    }

    @PostMapping
    @ResponseBody  // 
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequestDto dto, BindingResult result) {
        // 유효성 검사 실패
        if (result.hasErrors()) {
            // 필드별 에러 메시지 정리
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "입력값 오류입니다.",
                    "errors", errors
            ));
        }
        try {
            userService.registerUser(dto);
            return ResponseEntity.ok(Map.of(
                    "message", "회원가입 성공"
            ));
        } catch (IllegalStateException e) {
            Map<String, String> errors = new HashMap<>();
            if (e.getMessage().contains("아이디")) {
                errors.put("userId", e.getMessage());
            }
            if (e.getMessage().contains("이메일")) {
                errors.put("email", e.getMessage());
            }
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "회원가입 실패",
                    "errors", errors
            ));
        }
    }
}
