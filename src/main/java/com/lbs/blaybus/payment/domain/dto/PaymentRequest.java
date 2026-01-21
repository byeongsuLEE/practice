package com.lbs.blaybus.payment.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 준비 요청 정보")
public record PaymentRequest(
        @Schema(description = "상품 아이디", example = "1") Long productId,
        @Schema(description = "결제 금액", example = "10000") Integer amount,
        @Schema(description = "상품 수량", example = "1") Integer quantity) {
}
