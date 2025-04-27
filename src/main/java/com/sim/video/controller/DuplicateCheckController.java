package com.sim.video.controller;

import com.sim.video.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// 중복검사
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DuplicateCheckController {

    private final UserRepository userRepository;

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam("value") String value) {
        boolean exists = userRepository.existsByUserId(value);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("value") String value) {
        boolean exists = userRepository.existsByEmail(value);
        return ResponseEntity.ok(Map.of("exists", exists));
    }


}
