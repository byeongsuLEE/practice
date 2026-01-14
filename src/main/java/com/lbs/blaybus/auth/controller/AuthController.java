package com.lbs.blaybus.auth.controller;

import com.lbs.blaybus.auth.dto.request.TokenReissueRequestDto;
import com.lbs.blaybus.auth.dto.response.TokenResponseDto;
import com.lbs.blaybus.auth.service.AuthService;
import com.lbs.blaybus.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : 인증 관련 API Controller
 **/

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwaggerApi {

    private final AuthService authService;

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponseDto>> reissueToken(@RequestBody TokenReissueRequestDto request) {
        TokenResponseDto tokenResponse = authService.reissueToken(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, tokenResponse));
    }
}