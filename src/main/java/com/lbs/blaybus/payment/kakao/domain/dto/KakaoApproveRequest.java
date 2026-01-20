package com.lbs.blaybus.payment.kakao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오페이 결제 승인 요청 정보")
public record KakaoApproveRequest(
        @Schema(description = "결제 승인 요청용 토큰", example = "pg_token_example") String pgToken,
        @Schema(description = "가맹점 주문 번호", example = "UUID (random)") String orderId,
        @Schema(description = "유저 아이디", example = "1") Long userId) {
}
