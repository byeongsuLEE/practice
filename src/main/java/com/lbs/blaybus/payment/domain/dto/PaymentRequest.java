package com.lbs.blaybus.payment.domain.dto;

import lombok.Getter;

public record PaymentRequest(String cid,
                             Long productId,
                             String quantity,
                             String total) {
}
