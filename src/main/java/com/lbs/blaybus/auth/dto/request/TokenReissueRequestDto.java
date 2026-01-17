package com.lbs.blaybus.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : JWT Token 재발급 요청 DTO
 **/

@Schema(description = "토큰 재발급 요청 DTO")
@Getter
public class TokenReissueRequestDto {

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}