# JWT 인증 모듈

JWT 기반 인증/인가 시스템 (AccessToken + RefreshToken)

## 📦 필요한 의존성

```gradle
// build.gradle
dependencies {
    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

    // Spring Security
    implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

## 📁 파일 구조

```
src/main/java/com/yourproject/security/jwt/
├── JwtTokenProvider.java          # JWT 생성/검증
├── JwtProperties.java              # JWT 설정
├── JWTAuthenticationFilter.java    # JWT 인증 필터
└── JWTExceptionFilter.java         # JWT 예외 처리 필터
```

## ⚙️ 설정 파일 (application.yml)

```yaml
jwt:
  # Base64로 인코딩된 256비트 이상의 시크릿 키
  # 생성 방법: echo -n "your-secret-key-min-256-bits-long" | base64
  secret: your-base64-encoded-secret-key-here

  # Access Token 만료 시간 (30분 = 1800000ms)
  access-expiration-time: 1800000

  # Refresh Token 만료 시간 (7일 = 604800000ms)
  refresh-expiration-time: 604800000
```

### JWT Secret 키 생성 방법

```bash
# PowerShell (Windows)
[Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("your-secret-key-at-least-256-bits-long-please"))

# Bash (Mac/Linux)
echo -n "your-secret-key-at-least-256-bits-long-please" | base64

# 온라인 도구: https://www.base64encode.org/
```

## 🔐 SecurityConfig 설정

```java
package com.yourproject.security;

import com.yourproject.security.jwt.JWTAuthenticationFilter;
import com.yourproject.security.jwt.JWTExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            // JWT 필터 추가 (순서 중요!)
            .addFilterBefore(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

## 🚀 사용 방법

### 1. 로그인 시 토큰 발급

```java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // 사용자 인증 로직 (DB 조회 등)
        User user = userService.authenticate(request.getEmail(), request.getPassword());

        // 권한 정보
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_USER")
        );

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(
            user.getEmail(),
            user.getName(),
            authorities
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }
}
```

### 2. API 요청 시 토큰 사용

```bash
# HTTP 요청 헤더에 토큰 추가
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
```

### 3. 컨트롤러에서 인증된 사용자 정보 가져오기

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // 사용자 정보 조회
        User user = userService.findByEmail(userEmail);

        return ResponseEntity.ok(new UserResponse(user));
    }
}
```

### 4. RefreshToken으로 AccessToken 재발급

```java
@PostMapping("/refresh")
public ResponseEntity<TokenResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
    if (jwtTokenProvider.validateToken(refreshToken)) {
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();

        // 새로운 AccessToken 발급
        User user = userService.findByEmail(email);
        String newAccessToken = jwtTokenProvider.generateAccessToken(
            user.getEmail(),
            user.getName(),
            user.getAuthorities()
        );

        return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
```

## 📝 응답 형태

### 성공 시
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### JWT 에러 시
```json
{
  "status": 401,
  "message": "만료된 JWT 토큰입니다.",
  "timestamp": 1704801234567
}
```

## 💡 해커톤 꿀팁

### 1. 빠른 테스트용 설정
```yaml
jwt:
  secret: aGFja2F0aG9uLXRlc3Qta2V5LWZvci1xdWljay1kZXZlbG9wbWVudC0yNTY=
  access-expiration-time: 86400000  # 24시간 (개발 편의성)
  refresh-expiration-time: 604800000  # 7일
```

### 2. Postman/Thunder Client 사용 시
- Environment Variable에 토큰 저장
```javascript
// Tests 탭에 추가
pm.environment.set("accessToken", pm.response.json().accessToken);
```

- 다른 요청에서 사용
```
Authorization: Bearer {{accessToken}}
```

### 3. 프론트엔드에서 사용 (React/Vue 예시)
```javascript
// axios interceptor로 자동으로 토큰 추가
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

## ⚠️ 주의사항

1. **Secret 키는 절대 커밋하지 말 것** (.env 파일 사용 권장)
2. **HTTPS 사용 필수** (프로덕션 환경)
3. **RefreshToken은 Redis 등에 저장하여 관리** (선택사항)