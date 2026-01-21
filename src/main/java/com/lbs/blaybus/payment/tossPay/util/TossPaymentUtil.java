package com.lbs.blaybus.payment.tossPay.util;

import com.lbs.blaybus.payment.tossPay.domain.dto.TossApproveRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TossPaymentUtil {
    @Value("${toss.payment.secret-key}")
    private String secretKey;

    @Value("${toss.payment.host}")
    private String host;

    public <T> T sendRequest(String requestType, Map<String, Object> parameters, Class<T> responseType) {
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        return new RestTemplate().postForObject(
                host + requestType,
                requestEntity,
                responseType);
    }

    public Map<String, Object> confirmParameter(TossApproveRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("paymentKey", request.paymentKey());
        parameters.put("orderId", request.orderId());
        parameters.put("amount", request.amount());

        return parameters;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", getAuthorization());
        return headers;
    }

    public String getAuthorization() {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }
}
