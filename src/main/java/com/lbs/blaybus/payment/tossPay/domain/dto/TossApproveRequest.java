package com.lbs.blaybus.payment.tossPay.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토스페이 결제 승인 요청 정보")
public record TossApproveRequest(
        @Schema(description = "결제 키", example = "pay-12345") String paymentKey,
        @Schema(description = "주문 번호", example = "UUID (random)") String orderId,
        @Schema(description = "결제 금액", example = "10000") Integer amount,
        @Schema(description = "유저 아이디", example = "1") Long userId) {
}
