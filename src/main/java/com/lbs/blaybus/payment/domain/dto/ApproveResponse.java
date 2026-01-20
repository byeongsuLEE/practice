package com.lbs.blaybus.payment.domain.dto;

import com.lbs.blaybus.payment.domain.Amount;
import com.lbs.blaybus.payment.domain.Card;

public abstract class ApproveResponse {
    public abstract String getOrderId();

    public abstract String getTid();

    public abstract String getProductName();

    public abstract int getQuantity();

    public abstract String getApprovedAt();

    public abstract String getPayload();

    public abstract String getPaymentMethod();

    public abstract Amount toAmount();

    public abstract Card toCardInfo();
}
