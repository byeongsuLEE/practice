package com.lbs.blaybus.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성, 검증, 파싱을 담당하는 클래스
 */
@Component
@Slf4j
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * AccessToken 생성
     */
    public String generateAccessToken(String email, String name, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtProperties.getAccessExpirationTime());

        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(email)
                .claim("auth", authoritiesString)
                .claim("name", name)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(key)
                .compact();
    }

    /**
     * RefreshToken 생성
     */
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtProperties.getRefreshExpirationTime());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(key)
                .compact();
    }

    /**
     * AccessToken에서 Claims 추출
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰에서 사용자 이름 추출
     */
    public String getUserName(String token) {
        Claims claims = parseClaims(token);
        return claims.get("name", String.class);
    }

    /**
     * 토큰에서 Authentication 객체 추출
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String userEmail = claims.getSubject();
        String authorities = claims.get("auth", String.class);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (authorities != null && !authorities.isEmpty()) {
            authorityList = Arrays.stream(authorities.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        User user = new User(userEmail, "", authorityList);
        return new UsernamePasswordAuthenticationToken(user, null, authorityList);
    }

    /**
     * HTTP 요청에서 토큰 추출 (Authorization 헤더에서)
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new JwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new JwtException("JWT 토큰이 잘못되었습니다.");
        }
    }
}