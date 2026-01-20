package com.lbs.blaybus.product.exception;

import com.lbs.blaybus.common.exception.BusinessException;
import com.lbs.blaybus.common.response.ErrorCode;

public class ProductException extends BusinessException {
    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
