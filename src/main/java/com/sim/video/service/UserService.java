package com.sim.video.service;

import com.sim.video.domain.user.User;
import com.sim.video.dto.LoginRequestDto;
import com.sim.video.dto.RegisterRequestDto;
import com.sim.video.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final필드 대상 생성자 자동 생성
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    //회원가입
    @Transactional // 메시드 수행 시 예외발생시 자동 롤백
    public void registerUser(RegisterRequestDto dto){
        // 중복 처리
        if (userRepository.existsByUserId(dto.userId())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (dto.email() !=null && userRepository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        User user=User.builder()
                .userId(dto.userId())
                .password(passwordEncoder.encode(dto.password()))
                .email(dto.email())
                .role(User.Role.U)
                .age(dto.age())
                .gen(dto.gen())
                .phoneNum(dto.phoneNum())
                .build();
        userRepository.save(user);
    }
    
    //로그인 처리
    public void login(LoginRequestDto dto){
        User user=userRepository.findByUserId(dto.userId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 아이디입니다."));
        if (!passwordEncoder.matches(dto.password(),user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        //세션 생성 등 추후 추가
    }
}
