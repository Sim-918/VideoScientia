package com.sim.video.dto;

import jakarta.validation.constraints.NotBlank;

//이제 이 코드도 필요없음

public record LoginRequestDto(
        @NotBlank(message = "아이디를 입력해주세요.")
        String userId,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
