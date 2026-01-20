package com.lbs.blaybus.payment.application;

import com.lbs.blaybus.payment.domain.dto.PaymentApproveRequest;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.user.entity.User;

public interface PaymentService<T, R extends PaymentApproveRequest> {
    T ready(PaymentRequest request, User user);

    void approve(R approveRequest);

    void fail(String orderId);

    void cancel(String orderId);

    String getProvider();
}
