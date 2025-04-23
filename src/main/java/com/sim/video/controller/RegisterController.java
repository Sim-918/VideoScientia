package com.sim.video.controller;

import com.sim.video.dto.RegisterRequestDto;
import com.sim.video.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String register(@Valid @ModelAttribute RegisterRequestDto dto, Model model){
        try{
            userService.registerUser(dto);
            return "redirect:/login";
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "register";
        }
    }
}
