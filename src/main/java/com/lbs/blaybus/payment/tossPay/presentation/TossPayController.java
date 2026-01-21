package com.lbs.blaybus.payment.tossPay.presentation;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.tossPay.application.TossPaymentService;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveRequest;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveResponse;
import com.lbs.blaybus.payment.tossPay.domain.dto.TossReadyResponse;
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

@Tag(name = "Toss Payment", description = "토스페이 결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payment/toss")
public class TossPayController {
        private final TossPaymentService paymentService;

        @Operation(summary = "결제 준비 요청", description = "토스페이 결제를 시작하기 위해 주문 정보를 저장하고 준비 응답을 반환합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 준비 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"BAD_REQUEST\", \"message\": \"요청한 리소스를 찾을 수 없습니다\", \"data\": null}")))
        })
        @PostMapping("/ready")
        public ResponseEntity<ApiResponse<TossReadyResponse>> pickupPaymentRequest(
                        @RequestBody PaymentRequest paymentRequest) {
                // TODO : Principal User로 변경 필요
                User user = new User(1L, "테스트", "테스트", "테스트");
                TossReadyResponse ready = paymentService.ready(paymentRequest, user);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success(HttpStatus.OK, ready));
        }

        @Operation(summary = "결제 승인 요청", description = "토스페이 결제창에서 인증 후 반환된 paymentKey를 사용하여 최종 결제 승인을 요청합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 승인 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "결제 승인 실패", content = @Content(mediaType = "application/json", examples = {
                                        @ExampleObject(name = "금액 불일치", value = "{\"status\": \"BAD_REQUEST\", \"message\": \"결제 승인 단계 실패\", \"data\": null}"),
                                        @ExampleObject(name = "유효하지 않은 키", value = "{\"status\": \"BAD_REQUEST\", \"message\": \"잘못된 입력값입니다\", \"data\": null}")
                        })),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "주문 정보를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"NOT_FOUND\", \"message\": \"해당 주문 정보를 찾을 수 없습니다\", \"data\": null}")))
        })
        @GetMapping("/approve")
        public ResponseEntity<ApiResponse<TossApproveResponse>> tossPaymentConfirm(
                        @RequestParam("paymentKey") String paymentKey,
                        @RequestParam("orderId") String orderId,
                        @RequestParam("amount") int amount) {
                // TODO : Principal User로 변경 필요
                User user = new User(1L, "테스트", "테스트", "테스트");

                TossApproveRequest request = new TossApproveRequest(paymentKey, orderId, amount, user.getId());
                paymentService.approve(request);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success(HttpStatus.OK, null));
        }

        @Operation(summary = "결제 실패 콜백", description = "결제 진행 중 실패했을 때 호출되는 콜백 URL입니다.")
        @GetMapping("/fail")
        public ResponseEntity<ApiResponse<Void>> fail(@RequestParam("orderId") String orderId) {
                paymentService.fail(orderId);

                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                .body(ApiResponse.success(HttpStatus.PAYMENT_REQUIRED, null));
        }

        @Operation(summary = "결제 취소 콜백", description = "사용자가 결제를 직접 취소했을 때 호출되는 콜백 URL입니다.")
        @GetMapping("/cancel")
        public ResponseEntity<ApiResponse<Void>> cancel(@RequestParam("orderId") String orderId) {
                paymentService.cancel(orderId);

                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                .body(ApiResponse.success(HttpStatus.PAYMENT_REQUIRED, null));
        }
}
