package com.lbs.blaybus.user.dto.request;

import com.lbs.blaybus.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-13
 * 풀이방법
 **/

@Schema(description = "유저 가입 요청 DTO")
@Getter
public class JoinUserRequestDto {

    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    public User mapToDomain(JoinUserRequestDto request) {
        return User.builder()
                .email(request.email)
                .name(request.name)
                .phone(request.phone)
                .build();
    }
}
