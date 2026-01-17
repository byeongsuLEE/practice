package com.lbs.blaybus.auth.service;

import com.lbs.blaybus.auth.dto.response.TokenResponseDto;
import com.lbs.blaybus.auth.entity.RefreshTokenEntity;
import com.lbs.blaybus.auth.repository.RefreshTokenRepository;
import com.lbs.blaybus.common.exception.UserException;
import com.lbs.blaybus.common.jwt.JwtProperties;
import com.lbs.blaybus.common.jwt.JwtTokenProvider;
import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.user.entity.UserEntity;
import com.lbs.blaybus.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : 인증 관련 서비스 구현체
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto reissueToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserException(ErrorCode.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UserException(ErrorCode.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."));

        if (refreshTokenEntity.isExpired()) {
            throw new UserException(ErrorCode.UNAUTHORIZED, "만료된 Refresh Token입니다.");
        }

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getName(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtProperties.getAccessExpirationTime())
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDto generateToken(String email, String name) {
        String accessToken = jwtTokenProvider.generateAccessToken(
                email,
                name,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        saveRefreshToken(email, refreshToken, jwtProperties.getRefreshExpirationTime());

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtProperties.getAccessExpirationTime())
                .build();
    }

    @Override
    @Transactional
    public void saveRefreshToken(String email, String refreshToken, long expirationTime) {
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expirationTime / 1000);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByEmail(email)
                .map(entity -> {
                    entity.updateToken(refreshToken, expiryDate);
                    return entity;
                })
                .orElse(RefreshTokenEntity.builder()
                        .email(email)
                        .token(refreshToken)
                        .expiryDate(expiryDate)
                        .build());

        refreshTokenRepository.save(refreshTokenEntity);
    }
}