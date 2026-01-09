package com.lbs.blaybus.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션에서 발생하는 에러 코드 정의
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ========== 공통 에러 ==========
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),
    RUNTIME_EXCEPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Runtime 에러가 발생했습니다"),

    // ========== 인증/인가 에러 ==========
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

    // ========== JWT 에러 ==========
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다"),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "사용이 금지된 토큰입니다"),

    // ========== 리소스 에러 ==========
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),

    // ========== OAuth2 에러 ==========
    OAUTH2_CLIENT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "지원하지 않는 OAuth2 제공자입니다"),

    // ========== 비즈니스 로직 에러 ==========
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다");

    private final HttpStatus status;
    private final String message;
}