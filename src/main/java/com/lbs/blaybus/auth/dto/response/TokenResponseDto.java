package com.lbs.blaybus.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : JWT Token 응답 DTO
 **/

@Schema(description = "토큰 응답 DTO")
@Getter
@Builder
public class TokenResponseDto {

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Token Type", example = "Bearer")
    private String tokenType;

    @Schema(description = "Access Token 만료 시간 (밀리초)", example = "3600000")
    private Long accessTokenExpiresIn;
}