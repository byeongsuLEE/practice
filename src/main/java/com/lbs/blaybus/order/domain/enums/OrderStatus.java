package com.lbs.blaybus.order.domain.enums;

public enum OrderStatus {
    READY, // 결제 준비 (대기)
    APPROVE, // 결제 완료
    FAIL, // 결제 실패
    CANCEL // 결제 취소
}
