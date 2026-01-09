package com.lbs.blaybus.common.exception;

import com.lbs.blaybus.common.exception.BusinessException;
import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.common.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 모든 컨트롤러에서 발생하는 예외를 한 곳에서 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스텀 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage(), errorCode));
    }

    /**
     * Validation 예외 처리 (@Valid, @Validated)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e) {
        log.error("Validation Exception: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.success(HttpStatus.BAD_REQUEST, "입력값 검증 실패", errors));
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * RuntimeException 처리
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.RUNTIME_EXCEPTION_ERROR;
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage(), errorCode));
    }

    /**
     * 최상위 Exception 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getMessage(), errorCode));
    }
}