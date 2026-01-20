package com.lbs.blaybus.payment.kakao.domain.dto;

import com.lbs.blaybus.payment.domain.Card;
import com.lbs.blaybus.payment.domain.dto.ApproveResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "카카오페이 결제 승인 응답 정보")
@Getter
public class KakaoApproveResponse extends ApproveResponse {
    @Schema(description = "요청 고유 번호")
    private String aid;
    @Schema(description = "결제 고유 번호", example = "T1234567890123456789")
    private String tid;
    @Schema(description = "가맹점 코드")
    private String cid;
    @Schema(description = "정기결제용 ID")
    private String sid;
    @Schema(description = "가맹점 주문 번호", example = "order-1234")
    private String partner_order_id;
    @Schema(description = "가맹점 회원 id", example = "user-1234")
    private String partner_user_id;
    @Schema(description = "결제 수단", example = "CARD")
    private String payment_method_type;

    @Schema(description = "결제 금액 정보")
    private Amount amount;
    @Schema(description = "카드 결제 정보")
    private CardInfo card_info;
    @Schema(description = "상품명", example = "테스트 상품")
    private String item_name;
    @Schema(description = "상품 코드")
    private String item_code;
    @Schema(description = "상품 수량", example = "1")
    private Integer quantity;
    @Schema(description = "결제 요청 시간")
    private String created_at;
    @Schema(description = "결제 승인 시간")
    private String approved_at;
    @Schema(description = "결제 승인 요청에 대해 저장 값, 요청 시 전달 내용")
    private String payload;

    @Override
    public String getOrderId() {
        return partner_order_id;
    }

    @Override
    public String getTid() {
        return tid;
    }

    @Override
    public String getProductName() {
        return item_name;
    }

    @Override
    public int getQuantity() {
        return quantity != null ? quantity : 0;
    }

    @Override
    public String getApprovedAt() {
        return approved_at;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public String getPaymentMethod() {
        return payment_method_type;
    }

    @Override
    public com.lbs.blaybus.payment.domain.Amount toAmount() {
        if (this.amount == null)
            return null;
        return com.lbs.blaybus.payment.domain.Amount.builder()
                .total(this.amount.total)
                .taxFree(this.amount.tax_free)
                .vat(this.amount.vat)
                .point(this.amount.point)
                .discount(this.amount.discount)
                .build();
    }

    @Override
    public Card toCardInfo() {
        if (this.card_info == null)
            return null;
        return Card.builder()
                .purchaseCorp(this.card_info.kakaopay_purchase_corp)
                .purchaseCorpCode(this.card_info.kakaopay_purchase_corp_code)
                .issuerCorp(this.card_info.kakaopay_issuer_corp)
                .issuerCorpCode(this.card_info.kakaopay_issuer_corp_code)
                .bin(this.card_info.bin)
                .cardType(this.card_info.card_type)
                .installMonth(this.card_info.install_month)
                .approvedId(this.card_info.approved_id)
                .cardMid(this.card_info.card_mid)
                .interestFreeInstall(this.card_info.interest_free_install)
                .installmentType(this.card_info.installment_type)
                .cardItemCode(this.card_info.card_item_code)
                .build();
    }

    @Schema(description = "결제 금액 세부 정보")
    @Getter
    public static class Amount {
        @Schema(description = "총 결제 금액", example = "10000")
        private Integer total;
        @Schema(description = "비과세 금액", example = "0")
        private Integer tax_free;
        @Schema(description = "부가세 금액", example = "909")
        private Integer vat;
        @Schema(description = "사용한 포인트 금액", example = "0")
        private Integer point;
        @Schema(description = "할인 금액", example = "0")
        private Integer discount;
    }

    @Schema(description = "카드 결제 세부 정보")
    @Getter
    public static class CardInfo {
        @Schema(description = "카카오페이 매입사명")
        private String kakaopay_purchase_corp;
        @Schema(description = "카카오페이 매입사 코드")
        private String kakaopay_purchase_corp_code;
        @Schema(description = "카카오페이 발급사명")
        private String kakaopay_issuer_corp;
        @Schema(description = "카카오페이 발급사 코드")
        private String kakaopay_issuer_corp_code;
        @Schema(description = "카드 BIN")
        private String bin;
        @Schema(description = "카드 타입")
        private String card_type;
        @Schema(description = "할부 개월 수")
        private String install_month;
        @Schema(description = "카드사 승인 번호")
        private String approved_id;
        @Schema(description = "카드 가맹점 번호")
        private String card_mid;
        @Schema(description = "무이자할부 여부 (Y/N)")
        private String interest_free_install;
        @Schema(description = "할부 유형")
        private String installment_type;
        @Schema(description = "카드 상품 코드")
        private String card_item_code;
    }
}
