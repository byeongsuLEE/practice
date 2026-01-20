package com.lbs.blaybus.payment.tossPay.presentation;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.tossPay.application.TossPaymentService;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveRequest;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveResponse;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossReadyResponse;
import com.lbs.blaybus.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payment/toss")
public class TossPaymentController {
    private final TossPaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<ApiResponse<TossReadyResponse>> pickupPaymentRequest(@RequestBody PaymentRequest paymentRequest) {
        // TODO : Principal User로 변경 필요
        User user = new User(1L, "테스트", "테스트", "테스트");
        TossReadyResponse ready = paymentService.ready(paymentRequest, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, ready));
    }

    @GetMapping("/approve")
    public ResponseEntity<ApiResponse<TossApproveResponse>> tossPaymentConfirm(@RequestParam("paymentKey") String paymentKey,
                                                                               @RequestParam("orderId") String orderId,
                                                                               @RequestParam("amount") int amount) {
        // TODO : Principal User로 변경 필요
        User user = new User(1L, "테스트", "테스트", "테스트");

        TossApproveRequest request = new TossApproveRequest(paymentKey, orderId, amount, user.getId());
        paymentService.approve(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, null));
    }

    @GetMapping("/fail")
    public ResponseEntity<ApiResponse<Void>> fail(@RequestParam("orderId") String orderId) {
        paymentService.fail(orderId);

        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(ApiResponse.success(HttpStatus.PAYMENT_REQUIRED, null));
    }

    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(@RequestParam("orderId") String orderId) {
        paymentService.cancel(orderId);

        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(ApiResponse.success(HttpStatus.PAYMENT_REQUIRED, null));
    }
}
