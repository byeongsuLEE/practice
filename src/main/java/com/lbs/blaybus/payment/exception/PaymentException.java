package com.lbs.blaybus.payment.exception;

import com.lbs.blaybus.common.exception.BusinessException;
import com.lbs.blaybus.common.response.ErrorCode;

public class PaymentException extends BusinessException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
