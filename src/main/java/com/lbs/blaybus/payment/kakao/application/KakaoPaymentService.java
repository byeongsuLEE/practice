package com.lbs.blaybus.payment.kakao.application;

import com.lbs.blaybus.order.domain.Orders;
import com.lbs.blaybus.order.domain.dto.OrderDto;
import com.lbs.blaybus.order.domain.enums.OrderStatus;
import com.lbs.blaybus.order.infrastructure.OrderRepository;
import com.lbs.blaybus.payment.application.PaymentService;
import com.lbs.blaybus.payment.domain.Payment;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.infrastructure.PaymentRepository;
import com.lbs.blaybus.payment.kakao.domain.dto.ApproveResponse;
import com.lbs.blaybus.payment.kakao.domain.dto.KakaoApproveRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.ReadyResponse;
import com.lbs.blaybus.payment.kakao.util.KakaoPaymentUtil;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.product.exception.ProductException;
import com.lbs.blaybus.product.repository.ProductRepository;
import com.lbs.blaybus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static com.lbs.blaybus.common.response.ErrorCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class KakaoPaymentService implements PaymentService<ReadyResponse, KakaoApproveRequest> {
    private final KakaoPaymentUtil paymentUtil;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ReadyResponse ready(PaymentRequest request, User user) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductException(RESOURCE_NOT_FOUND));

        String orderId = UUID.randomUUID().toString();

        Map<String,Object> readyParam = paymentUtil.getReadyParameters(request, user.getId(), orderId, product);
        ReadyResponse kakaoReadyResponse = paymentUtil.sendRequest("/ready", readyParam, ReadyResponse.class);

        saveOrder(kakaoReadyResponse, request, orderId, user, product);

        return kakaoReadyResponse;
    }

    @Override
    public void approve(KakaoApproveRequest approveRequest) {
        Orders order = orderRepository.findByOrderId(approveRequest.orderId());

        Map<String, Object> approveParam = paymentUtil.getApproveParameters(approveRequest, order);

        ApproveResponse approveResponse = paymentUtil.sendRequest("/approve", approveParam, ApproveResponse.class);

        Payment payment = Payment.of(approveResponse);
        paymentRepository.save(payment);

        order.updateStatus(OrderStatus.APPROVE);
    }

    @Override
    public void fail(String orderId) {
        Orders order = orderRepository.findByOrderId(orderId);

        order.updateStatus(OrderStatus.FAIL);
    }

    @Override
    public void cancel(String orderId) {
        Orders order = orderRepository.findByOrderId(orderId);

        order.updateStatus(OrderStatus.CANCEL);
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    private void saveOrder(ReadyResponse readyResponse, PaymentRequest request,
                           String orderId, User user, Product product) {
        OrderDto dto = OrderDto.from(OrderStatus.READY, readyResponse.tid(), orderId, request.quantity(),
                getProvider(), user.getId(), product.getName());

        orderRepository.save(Orders.of(dto));
    }
}
