package com.sim.video.dto;


import com.sim.video.domain.user.User;
import jakarta.validation.constraints.*;

public record RegisterRequestDto (
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$",message = "아이디는 영문자+숫자 4~30자리여하 합니다.")
    String userId,

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*[~!@#$%^&*()_+=`{}\\\\[\\\\]:;\\\"'<>,.?/\\\\\\\\|-]).{8,300}$",message = "비밀번호는 영문, 숫자, 특수문자 포함 8자 이상")
    String password,

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Size(max = 30)
    String email,

    @Min(0) @Max(120)
    Integer age,

    @Pattern(regexp = "^\\\\d{11}$",message = "전화번호는 숫자 11자리여야 합니다")
    String phoneNum,

    @NotNull
    User.Role role,

    User.Gender gen
    ){

}
