package com.lbs.blaybus.order.infrastructure;

import com.lbs.blaybus.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderId(String orderId);
}
