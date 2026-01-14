package com.lbs.blaybus.auth.service;

import com.lbs.blaybus.auth.dto.response.TokenResponseDto;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : 인증 관련 서비스 인터페이스
 **/

public interface AuthService {

    TokenResponseDto reissueToken(String refreshToken);

    TokenResponseDto generateToken(String email, String name);

    void saveRefreshToken(String email, String refreshToken, long expirationTime);
}