package com.lbs.blaybus.payment.tossPay.domain.dto;

import com.lbs.blaybus.payment.domain.dto.ApproveResponse;
import com.lbs.blaybus.payment.tossPay.domain.TossPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "토스페이 결제 승인 응답")
@Getter
public class TossApproveResponse extends ApproveResponse {
    @Schema(description = "상점 아이디", example = "tosspayments")
    private String mId;
    @Schema(description = "마지막 거래 키", example = "trans-123")
    private String lastTransactionKey;
    @Schema(description = "결제 키 (식별자)", example = "pay-123456")
    private String paymentKey;
    @Schema(description = "주문 번호", example = "order-123")
    private String orderId;
    @Schema(description = "상품명", example = "테스트 상품")
    private String orderName;
    @Schema(description = "결제 상태", example = "DONE")
    private String status;
    @Schema(description = "결제 요청 시간", example = "2024-01-21T01:14:33+09:00")
    private String requestedAt;
    @Schema(description = "결제 승인 시간", example = "2024-01-21T01:15:33+09:00")
    private String approvedAt;
    @Schema(description = "결제 타입 정보 (NORMAL, BILLING, BRANDPAY)", example = "NORMAL")
    private TossPaymentType type;
    @Schema(description = "총 결제 금액", example = "10000")
    private int totalAmount;
    @Schema(description = "취소할 수 있는 금액 (잔고)", example = "10000")
    private int balanceAmount;
    @Schema(description = "공급가액", example = "9091")
    private int suppliedAmount;
    @Schema(description = "부가세", example = "909")
    private int vat;
    @Schema(description = "면세금액", example = "0")
    private int taxFreeAmount;
    @Schema(description = "결제수단", example = "카드")
    private String method;
    @Schema(description = "결제 승인 요청에 대해 저장 값, 요청 시 전달 내용")
    private String payload;
    @Setter
    @Schema(description = "상품 수량", example = "1")
    private int quantity;
    @Schema(description = "간편결제 정보")
    private EasyPay easyPay;
    @Schema(description = "카드 결제 정보")
    private Card card;

    @Override
    public String getTid() {
        return paymentKey;
    }

    @Override
    public String getProductName() {
        return orderName;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String getApprovedAt() {
        return approvedAt;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public String getPaymentMethod() {
        return method;
    }

    @Override
    public com.lbs.blaybus.payment.domain.Amount toAmount() {
        return com.lbs.blaybus.payment.domain.Amount.builder()
                .total(this.totalAmount)
                .taxFree(this.taxFreeAmount)
                .vat(this.vat)
                .point(0)
                .discount(this.easyPay != null ? this.easyPay.discountAmount : 0)
                .build();
    }

    @Override
    public com.lbs.blaybus.payment.domain.Card toCardInfo() {
        if (this.card == null)
            return null;
        return com.lbs.blaybus.payment.domain.Card.builder()
                .purchaseCorp(this.card.acquirerCode)
                .issuerCorp(this.card.issuerCode)
                .cardType(this.card.cardType)
                .installMonth(String.valueOf(this.card.installmentPlanMonths))
                .approvedId(this.card.approveNo)
                .build();
    }

    @Schema(description = "카드 결제 상세 정보")
    @Getter
    @NoArgsConstructor
    public static class Card {
        @Schema(description = "카드 발급사 코드")
        private String issuerCode;
        @Schema(description = "카드 매입사 코드")
        private String acquirerCode;
        @Schema(description = "카드번호", example = "12341234****1234")
        private String number;
        @Schema(description = "할부 개월 수", example = "0")
        private int installmentPlanMonths;
        @Schema(description = "카드사 승인 번호")
        private String approveNo;
        @Schema(description = "카드사 포인트 사용 여부")
        private boolean useCardPoint;
        @Schema(description = "카드 종류")
        private String cardType;
        @Schema(description = "카드 소유자 타입")
        private String ownerType;
        @Schema(description = "발행 영수증 URL")
        private String receiptUrl;
        @Schema(description = "카드사에 요청한 금액")
        private int amount;
    }

    @Schema(description = "간편결제 상세 정보")
    @Getter
    @NoArgsConstructor
    public static class EasyPay {
        @Schema(description = "간편결제 서비스 제공자")
        private String provider;
        @Schema(description = "결제 금액")
        private int amount;
        @Schema(description = "할인 금액")
        private int discountAmount;
    }

}
