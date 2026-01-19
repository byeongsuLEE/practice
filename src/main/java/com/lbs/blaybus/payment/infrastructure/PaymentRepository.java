package com.lbs.blaybus.payment.infrastructure;

import com.lbs.blaybus.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
