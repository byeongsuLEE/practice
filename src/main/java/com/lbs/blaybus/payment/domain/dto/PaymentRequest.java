package com.lbs.blaybus.payment.domain.dto;

import lombok.Getter;

public record PaymentRequest(Long productId,
                             Integer quantity) {
}
