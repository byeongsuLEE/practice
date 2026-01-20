package com.lbs.blaybus.payment.kakao.domain;

import com.lbs.blaybus.payment.domain.Amount;
import com.lbs.blaybus.payment.domain.Card;
import com.lbs.blaybus.payment.domain.Payment;

import java.util.Optional;

public interface PaymentApproval {
    String getOrderId();

    String getProductName();

    int getQuantity();

    String getPaymentMethod();

    Amount toAmount();

    Card toCardInfo();
}
