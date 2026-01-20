package com.lbs.blaybus.payment.kakao.presentation;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.kakao.application.KakaoPaymentService;
import com.lbs.blaybus.payment.kakao.domain.dto.KakaoApproveRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.ReadyResponse;
import com.lbs.blaybus.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Kakao Payment", description = "카카오페이 결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/kakao")
public class KakaoPayController implements KakaoPaySwagger {
    private final KakaoPaymentService paymentService;

    @PostMapping("/ready")
    public ResponseEntity<ApiResponse<ReadyResponse>> ready(@RequestBody PaymentRequest paymentRequest) {
        // TODO : Principal User로 변경 필요
        User user = new User(1L, "테스트", "테스트", "테스트");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, paymentService.ready(paymentRequest, user)));
    }

    @GetMapping("/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@RequestParam("pg_token") String pgToken,
                                                     @RequestParam("orderId") String orderId,
                                                     @RequestParam("userId") Long userId) {
        KakaoApproveRequest approveRequest = new KakaoApproveRequest(pgToken, orderId, userId);
        paymentService.approve(approveRequest);

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
