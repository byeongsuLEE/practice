package com.lbs.blaybus.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 응답을 통일된 형태로 반환하기 위한 클래스
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
public class ApiResponse<T> {

    private final HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // null이면 JSON에서 제외
    private final T data;

    /**
     * 성공 응답 (데이터 + 메시지)
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    /**
     * 성공 응답 (데이터만)
     */
    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(status, null, data);
    }

    /**
     * 성공 응답 (메시지만)
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }

    /**
     * 에러 응답
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getMessage(), null);
    }

    /**
     * 에러 응답 (ErrorCode 없이)
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}