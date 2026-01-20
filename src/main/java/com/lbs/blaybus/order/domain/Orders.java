package com.lbs.blaybus.order.domain;

import com.lbs.blaybus.common.jpa.BaseEntity;
import com.lbs.blaybus.order.domain.dto.OrderDto;
import com.lbs.blaybus.order.domain.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String orderId;

    private String tid;

    private Integer quantity;

    private String provider;

    private Long userId;

    private String productName;

    @Builder
    private Orders(OrderStatus status, String orderId, String tid, Integer quantity,
                   String provider, Long userId, String productName) {
        this.status = status;
        this.tid = tid;
        this.orderId = orderId;
        this.quantity = quantity;
        this.provider = provider;
        this.userId = userId;
        this.productName = productName;
    }

    public static Orders of(OrderDto dto) {
        return Orders.builder()
                .status(dto.status())
                .orderId(dto.orderId())
                .tid(dto.tid())
                .quantity(dto.quantity())
                .provider(dto.provider())
                .userId(dto.userId())
                .productName(dto.productName())
                .build();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
