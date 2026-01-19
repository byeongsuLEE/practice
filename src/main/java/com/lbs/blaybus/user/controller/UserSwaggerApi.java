package com.lbs.blaybus.user.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.user.dto.request.UserJoinRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "유저 API")
public interface UserSwaggerApi {

    @Operation(
            summary = "테스트 API",
            description = "API 연결 테스트용 엔드포인트입니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    String test();

    @Operation(
            summary = "유저 조회",
            description = "ID로 특정 유저의 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "유저 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없음"
            )
    })
    ResponseEntity<ApiResponse<UserResponseDto>> getUser(
            @Parameter(description = "조회할 유저의 ID", required = true, example = "1")
            @PathVariable(value = "id") Long id
    );

    @Operation(
            summary = "유저 가입",
            description = "새로운 유저를 등록합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "유저 가입 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일"
            )
    })
    ResponseEntity<ApiResponse<UserResponseDto>> joinUser(
            @Parameter(description = "가입할 유저 정보", required = true)
            @RequestBody UserJoinRequestDto request
    );
}
