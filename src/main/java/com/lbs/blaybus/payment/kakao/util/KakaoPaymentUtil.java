package com.lbs.blaybus.payment.kakao.util;

import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.ReadyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoPaymentUtil<T> {
    private final String SUCCESS = "/payment/success?memberId=%s";
    private final String FAILURE = "/payment/fail?memberId=%s&productName=%s&quantity=%s";
    private final String CANCEL = "/payment/fail?memberId=%s&productName=%s&quantity=%s";

    @Value("${kakao.payment.cid}")
    private String CID;

    @Value("${kakao.payment.secret-key-dev}")
    private String secretKey;

    @Value("${kakao.payment.host}")
    private String host;

    @Value("${kakao.payment.request-url}")
    private String requestUrl;

    public ReadyResponse sendRequest(String requestType, Map<String, Object> parameters) {
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        return new RestTemplate().postForObject(
                host + requestType,
                requestEntity,
                ReadyResponse.class);
    }

    public Map<String, Object> getReadyParameters(PaymentRequest paymentRequest, Long memberId,
                                                          String partnerOrderId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", CID);
        parameters.put("partner_order_id", partnerOrderId);
        parameters.put("partner_user_id", memberId.toString());
        parameters.put("item_name", "테스트 상품"); // TODO : 엔티티 생성 후 변경
        parameters.put("quantity", paymentRequest.quantity());
        parameters.put("total_amount", paymentRequest.total());
        parameters.put("vat_amount", 0);
        parameters.put("tax_free_amount", 0);
        parameters.put("approval_url", requestUrl + String.format(SUCCESS, memberId));
        parameters.put("cancel_url", requestUrl
                + String.format(CANCEL, memberId, "테스트 상품", paymentRequest.quantity()));
        parameters.put("fail_url", requestUrl
                + String.format(FAILURE, memberId, "테스트 상품", paymentRequest.quantity()));
        return parameters;
    }


    public Map<String, Object> getApproveParameters(Long memberId, String orderId, String transactionId,
                                                            String pgToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", CID);
        parameters.put("tid", transactionId);
        parameters.put("partner_order_id", orderId);
        parameters.put("partner_user_id", memberId.toString());
        parameters.put("pg_token", pgToken);
        return parameters;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        return headers;
    }
}
