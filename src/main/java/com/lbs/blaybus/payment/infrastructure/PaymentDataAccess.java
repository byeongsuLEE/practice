package com.lbs.blaybus.payment.infrastructure;

import com.lbs.blaybus.order.domain.Orders;
import com.lbs.blaybus.order.domain.dto.OrderDto;
import com.lbs.blaybus.order.domain.enums.OrderStatus;
import com.lbs.blaybus.order.infrastructure.OrderRepository;
import com.lbs.blaybus.payment.domain.Payment;
import com.lbs.blaybus.payment.domain.dto.ApproveResponse;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.product.exception.ProductException;
import com.lbs.blaybus.product.repository.ProductRepository;
import com.lbs.blaybus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.lbs.blaybus.common.response.ErrorCode.RESOURCE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PaymentDataAccess {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(RESOURCE_NOT_FOUND));
    }

    public Orders findOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public void saveOrder(PaymentRequest request, String orderId, String tid, String provider, User user, Product product) {
        OrderDto dto = OrderDto.from(OrderStatus.READY, tid, orderId, request.quantity(),
                request.amount(), provider, user.getId(), product.getName());

        orderRepository.save(Orders.of(dto));
    }

    public void savePayment(ApproveResponse approveResponse, Orders order) {
        Payment payment = Payment.of(approveResponse, order.getUserId());

        paymentRepository.save(payment);
    }
}
