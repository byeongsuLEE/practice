package com.lbs.blaybus.payment.domain.enums;

import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.payment.exception.PaymentException;

import java.util.Arrays;

public enum PaymentMethod {
    CARD, MONEY,

    // TossMethod
    카드,
    가상계좌,
    간편결제,
    휴대폰,
    계좌이체,
    문화상품권,
    도서문화상품권,
    게임문화상품권;

    /**
     * String 값을 기반으로 Enum 값을 반환하는 메서드
     *
     * @param name 문자열 값
     * @return 해당 문자열에 매핑되는 Enum 값
     * @throws IllegalArgumentException 매핑되는 Enum 값이 없을 경우 예외 발생
     */
    public static PaymentMethod fromString(String name) {
        return Arrays.stream(PaymentMethod.values())
                .filter(value -> value.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new PaymentException(ErrorCode.INVALID_INPUT_VALUE));
    }
}
