package com.sim.video.dto;


import com.sim.video.domain.user.User;
import jakarta.validation.constraints.*;

public record RegisterRequestDto (
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$",message = "아이디는 영문자+숫자 4~30자리여하 합니다.")
    String userId,

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$", message = "비밀번호는 영문 대/소문자, 숫자, 특수문자를 포함해 8자 이상")
    String password,

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Size(max = 30)
    String email,

    Integer age,

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "010-0000-0000 형식이어야 합니다")
    String phoneNum,

    @NotNull
    User.Gender gen
    ){

}
