package com.lbs.blaybus.common.jwt;

import com.lbs.blaybus.common.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 HTTP 요청에 대해 JWT 토큰을 검증하는 필터
 * 유효한 토큰이 있으면 SecurityContext에 인증 정보 저장
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
            }
        } catch (JwtException e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
            // 예외는 JWTExceptionFilter에서 처리됨
        }

        filterChain.doFilter(request, response);
    }
}