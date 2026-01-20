package com.lbs.blaybus.order.domain.dto;

import com.lbs.blaybus.order.domain.enums.OrderStatus;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.user.entity.User;

public record OrderDto(OrderStatus status,
                       String tid,
                       String orderId,
                       Integer quantity,
                       String provider,
                       Long userId,
                       String productName) {

    public static OrderDto from(OrderStatus status, String tid, String orderId, Integer quantity,
                       String provider, Long userId, String productName) {
        return new OrderDto(status, tid, orderId, quantity, provider, userId, productName);
    }
}
