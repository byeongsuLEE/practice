package com.lbs.blaybus.common.exception;

import com.lbs.blaybus.common.response.ErrorCode;
import lombok.Getter;

/**
 * 비즈니스 로직에서 발생하는 커스텀 예외
 * 사용 예시:
 *   if (user == null) {
 *       throw new BusinessException(ErrorCode.USER_NOT_FOUND);
 *   }
 */
@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}