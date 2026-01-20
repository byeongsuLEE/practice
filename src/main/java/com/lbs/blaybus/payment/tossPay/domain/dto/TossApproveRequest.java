package com.lbs.blaybus.payment.tossPay.domain.dto;

public record TossApproveRequest(String paymentKey,
                                 String orderId,
                                 Integer amount,
                                 Long userId) {
}
