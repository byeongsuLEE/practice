package com.lbs.blaybus.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 설정값을 application.yml에서 읽어오는 클래스
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;                    // JWT 서명 키 (Base64 인코딩된 값)
    private long accessExpirationTime;        // Access Token 만료 시간 (밀리초)
    private long refreshExpirationTime;       // Refresh Token 만료 시간 (밀리초)
}