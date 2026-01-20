package com.lbs.blaybus.payment.kakao.domain.dto;

import com.lbs.blaybus.payment.domain.dto.PaymentRequest;

public record KakaoApproveRequest(String pgToken,
                                  String orderId,
                                  Long userId) {
}
