package com.lbs.blaybus.payment.domain;

import com.lbs.blaybus.payment.domain.dto.ApproveResponse;
import com.lbs.blaybus.payment.domain.enums.PaymentMethod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tid; // 결제 고유 번호
    private String orderId; // 가맹점 주문번호
    private String productName; // 상품 이름
    private Integer quantity; // 상품 수량
    private String requestedAt; // 결제 준비 요청 시간
    private String approvedAt; // 결제 승인 시간
    private String payload; // 결제 승인 요청에 대해 저장 값, 요청 시 전달된 내용
    private Long memberId; // 결제한 유저 아이디

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethodType; // 결제 수단(CARD 또는 MONEY)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amount_id")
    private Amount amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_info_id")
    private Card cardInfo;

    @Builder
    private Payment(ApproveResponse approveResponse, Long userId) {
        this.tid = approveResponse.getTid();
        this.orderId = approveResponse.getOrderId();
        this.productName = approveResponse.getProductName();
        this.quantity = approveResponse.getQuantity();
        this.requestedAt = approveResponse.getApprovedAt();
        this.approvedAt = approveResponse.getApprovedAt();
        this.payload = approveResponse.getPayload();
        this.memberId = userId;
        this.paymentMethodType = PaymentMethod.fromString(approveResponse.getPaymentMethod());
        this.amount = approveResponse.toAmount();
        this.cardInfo = approveResponse.toCardInfo();
    }

    public static Payment of(ApproveResponse approveResponse, Long userId) {
        return Payment.builder()
                .approveResponse(approveResponse)
                .userId(userId)
                .build();
    }
}
