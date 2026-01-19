package com.lbs.blaybus.payment.domain;

import com.lbs.blaybus.payment.kakao.domain.dto.ApproveResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purchaseCorp; // 매입사명
    private String purchaseCorpCode; // 매입사 코드
    private String issuerCorp; // 발급사명
    private String issuerCorpCode; // 발급사 코드
    private String bin; // 카드 BIN
    private String cardType; // 카드 타입
    private String installMonth; // 할부 개월 수
    private String approvedId; // 카드사 승인번호
    private String cardMid; // 카드사 가맹점 번호
    private String interestFreeInstall; // 무이자할부 여부(Y/N)
    private String installmentType; // 할부 유형
    private String cardItemCode; // 카드 상품 코드

    @OneToOne(mappedBy = "cardInfo")
    private Payment Payment;

    @Builder
    private Card(ApproveResponse approveResponse) {
        this.approvedId = approveResponse.getCard_info().getApproved_id();
        this.bin = approveResponse.getCard_info().getBin();
        this.cardMid = approveResponse.getCard_info().getCard_mid();
        this.cardType = approveResponse.getCard_info().getCard_type();
        this.installMonth = approveResponse.getCard_info().getInstall_month();
        this.cardItemCode = approveResponse.getCard_info().getCard_item_code();
        this.installmentType = approveResponse.getCard_info().getInstallment_type();
        this.interestFreeInstall = approveResponse.getCard_info().getInterest_free_install();
        this.purchaseCorp = approveResponse.getCard_info().getKakaopay_purchase_corp();
        this.purchaseCorpCode = approveResponse.getCard_info().getKakaopay_purchase_corp_code();
        this.issuerCorp = approveResponse.getCard_info().getKakaopay_issuer_corp();
        this.issuerCorpCode = approveResponse.getCard_info().getKakaopay_issuer_corp_code();
    }

    public static Card createKakao(ApproveResponse approveResponse) {
        return Card.builder()
                .approveResponse(approveResponse)
                .build();
    }
}