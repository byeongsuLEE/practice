package com.lbs.blaybus.user.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "유저 응답 DTO")
@Builder
@Getter
public class UserResponseDto {

    @Schema(description = "유저 ID", example = "1")
    private Long id;

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "생성 일시", example = "2026-01-13T10:00:00")
    LocalDateTime createDateTime;

    @Schema(description = "수정 일시", example = "2026-01-13T10:00:00")
    LocalDateTime changeDateTime;

    @Schema(description = "생성자", example = "admin")
    String createBy;

    @Schema(description = "수정자", example = "admin")
    String changeBy;
}
