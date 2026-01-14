package com.lbs.blaybus.user.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Schema(description = "유저 응답 DTO")
@SuperBuilder
@Getter
public class UserResponseDto extends BaseDto {

    @Schema(description = "유저 ID", example = "1")
    private Long id;

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

}
