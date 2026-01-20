package com.lbs.blaybus.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Amount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer total; // 총 결제 금액
    private Integer taxFree; // 비과세 금액
    private Integer vat; // 부가세 금액
    private Integer point; // 사용한 포인트 금액
    private Integer discount; // 할인 금액

    @Builder
    private Amount(Integer total, Integer taxFree, Integer vat, Integer point, Integer discount) {
        this.total = total;
        this.taxFree = taxFree;
        this.vat = vat;
        this.point = point;
        this.discount = discount;
    }
}