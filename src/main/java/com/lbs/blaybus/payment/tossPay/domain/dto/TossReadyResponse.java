package com.lbs.blaybus.payment.tossPay.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossReadyResponse {
    private Long userId;
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
