package com.lbs.blaybus.payment.tossPay.application;

import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.order.domain.Orders;
import com.lbs.blaybus.order.domain.enums.OrderStatus;
import com.lbs.blaybus.payment.application.PaymentService;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.exception.PaymentException;
import com.lbs.blaybus.payment.infrastructure.PaymentDataAccess;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveRequest;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveResponse;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossReadyResponse;
import com.lbs.blaybus.payment.tossPay.util.TossPaymentUtil;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TossPaymentService implements PaymentService<TossReadyResponse, TossApproveRequest> {
    private final TossPaymentUtil paymentUtil;
    private final PaymentDataAccess paymentDataAccess;

    @Override
    public TossReadyResponse ready(PaymentRequest request, User user) {
        Product product = paymentDataAccess.findProductById(request.productId());

        String orderId = UUID.randomUUID().toString();

        paymentDataAccess.saveOrder(request, orderId, null, getProvider(), user, product);

        return TossReadyResponse.of(user.getId(), orderId);
    }

    @Override
    public void approve(TossApproveRequest approveRequest) {
        Orders order = paymentDataAccess.findOrderByOrderId(approveRequest.orderId());

        if(!validOrderIdAndAmount(approveRequest, order)) {
            throw new PaymentException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Map<String, Object> approveParam = paymentUtil.confirmParameter(approveRequest);

        TossApproveResponse approveResponse = paymentUtil.sendRequest("/confirm", approveParam, TossApproveResponse.class);

        approveResponse.setQuantity(order.getQuantity());
        order.setTid(approveResponse.getTid());

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
        return "TOSS";
    }

    private boolean validOrderIdAndAmount(TossApproveRequest approveRequest, Orders order) {
        return Objects.equals(order.getOrderId(), approveRequest.orderId())
                && Objects.equals(order.getAmount(), approveRequest.amount());
    }
}
