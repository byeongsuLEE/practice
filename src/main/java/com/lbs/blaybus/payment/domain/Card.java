package com.lbs.blaybus.payment.domain;

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
    private Card(String purchaseCorp, String purchaseCorpCode, String issuerCorp, String issuerCorpCode,
            String bin, String cardType, String installMonth, String approvedId, String cardMid,
            String interestFreeInstall, String installmentType, String cardItemCode) {
        this.purchaseCorp = purchaseCorp;
        this.purchaseCorpCode = purchaseCorpCode;
        this.issuerCorp = issuerCorp;
        this.issuerCorpCode = issuerCorpCode;
        this.bin = bin;
        this.cardType = cardType;
        this.installMonth = installMonth;
        this.approvedId = approvedId;
        this.cardMid = cardMid;
        this.interestFreeInstall = interestFreeInstall;
        this.installmentType = installmentType;
        this.cardItemCode = cardItemCode;
    }
}