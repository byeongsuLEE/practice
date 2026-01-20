package com.lbs.blaybus.payment.tossPay.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "토스페이 결제 준비 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossReadyResponse {
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;
    @Schema(description = "주문 번호", example = "UUID (random)")
    private String orderId;

    @Builder
    private TossReadyResponse(Long userId, String orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    public static TossReadyResponse of(Long userId, String orderId) {
        return TossReadyResponse.builder()
                .userId(userId)
                .orderId(orderId)
                .build();
    }
}
