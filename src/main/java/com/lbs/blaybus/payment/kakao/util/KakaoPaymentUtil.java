package com.lbs.blaybus.payment.kakao.util;

import com.lbs.blaybus.order.domain.Orders;
import com.lbs.blaybus.payment.domain.dto.PaymentRequest;
import com.lbs.blaybus.payment.kakao.domain.dto.KakaoApproveRequest;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoPaymentUtil {
    private final String SUCCESS = "/api/payment/kakao/approve?orderId=%s&userId=%s";
    private final String FAILURE = "/api/payment/kakao/fail?orderId=%s&userId=%s";
    private final String CANCEL = "/api/payment/kakao/fail?orderId=%s&userId=%s";

    @Value("${kakao.payment.cid}")
    private String CID;

    @Value("${kakao.payment.secret-key-dev}")
    private String secretKey;

    @Value("${kakao.payment.host}")
    private String host;

    @Value("${kakao.payment.request-url}")
    private String requestUrl;

    public <T> T sendRequest(String requestType, Map<String, Object> parameters, Class<T> classType) {
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        return new RestTemplate().postForObject(
                host + requestType,
                requestEntity,
                classType);
    }

    public Map<String, Object> getReadyParameters(PaymentRequest paymentRequest, Long userId,
                                                  String orderId, Product product) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", CID);
        parameters.put("partner_order_id", orderId);
        parameters.put("partner_user_id", userId.toString());
        parameters.put("item_name", product.getName());
        parameters.put("quantity", paymentRequest.quantity());
        parameters.put("total_amount", (product.getPrice() * paymentRequest.quantity()));
        parameters.put("vat_amount", 0);
        parameters.put("tax_free_amount", 0);
        parameters.put("approval_url", requestUrl + String.format(SUCCESS, orderId, userId));
        parameters.put("cancel_url", requestUrl
                + String.format(CANCEL, orderId, userId));
        parameters.put("fail_url", requestUrl
                + String.format(FAILURE, orderId, userId));
        return parameters;
    }


    public Map<String, Object> getApproveParameters(KakaoApproveRequest request, Orders order) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", CID);
        parameters.put("tid", order.getTid());
        parameters.put("partner_order_id", request.orderId());
        parameters.put("partner_user_id", request.userId());
        parameters.put("pg_token", request.pgToken());
        return parameters;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        return headers;
    }
}
