package com.lbs.blaybus.payment.kakao.domain.dto;

import com.lbs.blaybus.payment.domain.Card;
import com.lbs.blaybus.payment.domain.dto.ApproveResponse;
import lombok.Getter;

@Getter
public class KakaoApproveResponse extends ApproveResponse {
    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String sid; // 정기결제용 ID
    private String partner_order_id; // 가맹점 주문 번호
    private String partner_user_id; // 가맹점 회원 id
    private String payment_method_type; // 결제 수단

    private Amount amount;
    private CardInfo card_info;
    private String item_name; // 상품명
    private String item_code; // 상품 코드
    private Integer quantity; // 상품 수량
    private String created_at; // 결제 요청 시간
    private String approved_at; // 결제 승인 시간
    private String payload; // 결제 승인 요청에 대해 저장 값, 요청 시 전달 내용

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
        return quantity;
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

    @Getter
    public static class Amount {
        private Integer total; // 총 결제 금액
        private Integer tax_free; // 비과세 금액
        private Integer vat; // 부가세 금액
        private Integer point; // 사용한 포인트 금액
        private Integer discount; // 할인 금액
    }

    @Getter
    public static class CardInfo {
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
        private String bin;
        private String card_type;
        private String install_month;
        private String approved_id;
        private String card_mid;
        private String interest_free_install;
        private String installment_type;
        private String card_item_code;
    }
}
