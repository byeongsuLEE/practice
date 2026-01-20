package com.lbs.blaybus.payment.kakao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "카카오페이 결제 준비 응답")
public record ReadyResponse(
        @Schema(description = "결제 고유 번호", example = "T1234567890123456789") String tid,
        @Schema(description = "앱 결제 페이지 URL") String next_redirect_app_url,
        @Schema(description = "모바일 웹 결제 페이지 URL") String next_redirect_mobile_url,
        @Schema(description = "PC 웹 결제 페이지 URL") String next_redirect_pc_url,
        @Schema(description = "결제 준비 요청 시간") LocalDateTime created_at) {
}
