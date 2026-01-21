package com.lbs.blaybus.payment.kakao.presentation;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.ReadyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface KakaoPaySwagger {

        @Operation(summary = "결제 준비 요청", description = "카카오페이 결제를 시작하기 위해 TID를 발급받고 결제 페이지 URL을 응답받습니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 준비 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"BAD_REQUEST\", \"message\": \"결제 준비 단계 실패\", \"data\": null}"))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"NOT_FOUND\", \"message\": \"요청한 리소스를 찾을 수 없습니다\", \"data\": null}")))
        })
        @PostMapping("/ready")
        ResponseEntity<ApiResponse<ReadyResponse>> ready(@RequestBody PaymentRequest paymentRequest);

        @Operation(summary = "결제 승인 요청", description = "사용자가 결제 수단을 선택하고 인증을 마친 후 실제 결제를 승인합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 승인 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "결제 승인 실패", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"BAD_REQUEST\", \"message\": \"결제 승인 단계 실패\", \"data\": null}"))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "주문 정보를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"NOT_FOUND\", \"message\": \"해당 주문 정보를 찾을 수 없습니다\", \"data\": null}")))
        })
        @GetMapping("/approve")
        ResponseEntity<ApiResponse<Void>> approve(@RequestParam("pg_token") String pgToken,
                        @RequestParam("orderId") String orderId,
                        @RequestParam("userId") Long userId);

        @Operation(summary = "결제 실패 콜백", description = "결제 진행 중 실패했을 때 호출되는 콜백 URL입니다.")
        public ResponseEntity<ApiResponse<Void>> fail(@RequestParam("orderId") String orderId);

        @Operation(summary = "결제 취소 콜백", description = "사용자가 결제를 직접 취소했을 때 호출되는 콜백 URL입니다.")
        ResponseEntity<ApiResponse<Void>> cancel(@RequestParam("orderId") String orderId);
}
