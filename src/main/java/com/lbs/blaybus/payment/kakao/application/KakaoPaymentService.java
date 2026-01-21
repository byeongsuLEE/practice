package com.lbs.blaybus.payment.kakao.application;

import com.lbs.blaybus.order.domain.Orders;
import com.lbs.blaybus.order.domain.enums.OrderStatus;
import com.lbs.blaybus.payment.application.PaymentService;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.infrastructure.PaymentDataAccess;
import com.lbs.blaybus.payment.kakao.domain.dto.KakaoApproveResponse;
import com.lbs.blaybus.payment.kakao.domain.dto.KakaoApproveRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.ReadyResponse;
import com.lbs.blaybus.payment.kakao.util.KakaoPaymentUtil;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPaymentService implements PaymentService<ReadyResponse, KakaoApproveRequest> {
    private final KakaoPaymentUtil paymentUtil;
    private final PaymentDataAccess paymentDataAccess;

    @Override
    public ReadyResponse ready(PaymentRequest request, User user) {
        Product product = paymentDataAccess.findProductById(request.productId());

        String orderId = UUID.randomUUID().toString();

        Map<String,Object> readyParam = paymentUtil.getReadyParameters(request, user.getId(), orderId, product);
        ReadyResponse kakaoReadyResponse = paymentUtil.sendRequest("/ready", readyParam, ReadyResponse.class);

        paymentDataAccess.saveOrder(request, orderId, kakaoReadyResponse.tid(),getProvider(), user, product);

        return kakaoReadyResponse;
    }

    @Override
    public void approve(KakaoApproveRequest approveRequest) {
        Orders order = paymentDataAccess.findOrderByOrderId(approveRequest.orderId());

        Map<String, Object> approveParam = paymentUtil.getApproveParameters(approveRequest, order);

        KakaoApproveResponse approveResponse = paymentUtil.sendRequest("/approve", approveParam, KakaoApproveResponse.class);

        paymentDataAccess.savePayment(approveResponse, order);

        order.updateStatus(OrderStatus.APPROVE);
    }

    @Override
    public void fail(String orderId) {
        Orders order = paymentDataAccess.findOrderByOrderId(orderId);

        order.updateStatus(OrderStatus.FAIL);
    }

    @Override
    public void cancel(String orderId) {
        Orders order = paymentDataAccess.findOrderByOrderId(orderId);

        order.updateStatus(OrderStatus.CANCEL);
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }
}
