package com.lbs.blaybus.auth.controller;

import com.lbs.blaybus.auth.dto.response.TokenResponseDto;
import com.lbs.blaybus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : 인증 API Swagger 문서
 **/

@Tag(name = "Auth", description = "인증 API")
public interface AuthSwaggerApi {

    @Operation(
            summary = "JWT 토큰 재발급",
            description = "Authorization 헤더에 담긴 유효한 Refresh Token으로 새로운 Access Token을 발급합니다. Refresh Token은 만료되지 않았는지 검증 후, 새로운 Access Token만 반환합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Access Token 재발급 성공 (refreshToken 필드는 null)",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않거나 만료된 Refresh Token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            )
    })
    ResponseEntity<ApiResponse<TokenResponseDto>> reissueToken(
            @Parameter(description = "Refresh Token (형식: Bearer {token})", required = true, example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader(value = "Authorization") String authorization
    );
}