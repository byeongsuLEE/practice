# Redis 설정 모듈

Redis 연결 설정 및 기본 CRUD 유틸리티

## 📦 필요한 의존성

```gradle
// build.gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    // 또는 reactive 버전
    // implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
}
```

## ⚙️ 설정 파일 (application.yml)

```yaml
redis:
  host: localhost  # 또는 Redis 서버 IP
  port: 6379
  password: ""     # 비밀번호 없으면 빈 문자열
```

## 🐳 Docker로 Redis 실행

```bash
# 기본 Redis 실행 (비밀번호 없음)
docker run -d --name redis -p 6379:6379 redis:latest

# 비밀번호 설정
docker run -d --name redis -p 6379:6379 redis:latest redis-server --requirepass your-password

# docker-compose.yml
version: '3.8'
services:
  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"
    command: redis-server --requirepass your-password  # 선택사항
```

## 📁 파일 구조

```
src/main/java/com/yourproject/
├── config/
│   └── RedisConfiguration.java    # Redis 설정
└── service/
    └── RedisService.java           # Redis 유틸리티
```

## 🚀 사용 방법

### 1. 기본 CRUD

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisService redisService;

    // 저장
    public void cacheUser(User user) {
        String key = "user:" + user.getId();
        redisService.save(key, user);
    }

    // 저장 (5분 후 만료)
    public void cacheUserWithTTL(User user) {
        String key = "user:" + user.getId();
        redisService.save(key, user, Duration.ofMinutes(5));
    }

    // 조회
    public User getCachedUser(Long userId) {
        String key = "user:" + userId;
        return redisService.get(key, User.class);
    }

    // 삭제
    public void deleteCachedUser(Long userId) {
        String key = "user:" + userId;
        redisService.delete(key);
    }

    // 존재 여부 확인
    public boolean isCached(Long userId) {
        String key = "user:" + userId;
        return redisService.exists(key);
    }
}
```

### 2. RefreshToken 저장 (JWT와 함께 사용)

```java
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {
        // 사용자 인증 로직
        User user = authenticate(request);

        // 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        // Redis에 RefreshToken 저장 (7일 만료)
        String key = "refresh:" + user.getId();
        redisService.save(key, refreshToken, Duration.ofDays(7));

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        // 토큰 검증
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());

        // Redis에서 저장된 RefreshToken 조회
        String key = "refresh:" + userId;
        String savedToken = redisService.get(key, String.class);

        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 새로운 AccessToken 발급
        User user = userService.findById(userId);
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void logout(Long userId) {
        // RefreshToken 삭제
        String key = "refresh:" + userId;
        redisService.delete(key);
    }
}
```

### 3. 캐싱 전략 (Cache-Aside Pattern)

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RedisService redisService;
    private final ProductRepository productRepository;

    public Product findById(Long productId) {
        String key = "product:" + productId;

        // 1. 캐시에서 조회
        Product cachedProduct = redisService.get(key, Product.class);
        if (cachedProduct != null) {
            log.info("캐시 히트 - productId: {}", productId);
            return cachedProduct;
        }

        // 2. DB에서 조회
        log.info("캐시 미스 - DB 조회 - productId: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // 3. 캐시에 저장 (10분 TTL)
        redisService.save(key, product, Duration.ofMinutes(10));

        return product;
    }

    public void updateProduct(Product product) {
        // DB 업데이트
        productRepository.save(product);

        // 캐시 무효화
        String key = "product:" + product.getId();
        redisService.delete(key);
    }
}
```

### 4. 조회수 카운팅

```java
@Service
@RequiredArgsConstructor
public class PostService {

    private final RedisService redisService;

    // 조회수 증가
    public void incrementViewCount(Long postId) {
        String key = "post:view:" + postId;
        redisService.increment(key);
    }

    // 조회수 조회
    public Long getViewCount(Long postId) {
        String key = "post:view:" + postId;
        Object count = redisService.get(key);
        return count != null ? Long.parseLong(count.toString()) : 0L;
    }

    // 실시간 인기 게시물 (1시간 내 조회수 높은 순)
    public List<Long> getTrendingPosts() {
        Set<String> keys = redisService.getKeys("post:view:*");
        return keys.stream()
                .map(key -> {
                    Long postId = Long.parseLong(key.replace("post:view:", ""));
                    Long count = getViewCount(postId);
                    return new PostViewCount(postId, count);
                })
                .sorted(Comparator.comparing(PostViewCount::getCount).reversed())
                .limit(10)
                .map(PostViewCount::getPostId)
                .collect(Collectors.toList());
    }
}
```

### 5. Rate Limiting (API 호출 제한)

```java
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisService redisService;

    /**
     * 1분에 10회 제한
     */
    public boolean isAllowed(String userId) {
        String key = "rate-limit:" + userId;

        // 현재 카운트 조회
        Object count = redisService.get(key);

        if (count == null) {
            // 첫 요청
            redisService.save(key, 1, Duration.ofMinutes(1));
            return true;
        }

        int currentCount = Integer.parseInt(count.toString());
        if (currentCount >= 10) {
            return false;  // 제한 초과
        }

        // 카운트 증가
        redisService.increment(key);
        return true;
    }
}

// 사용 예시 (Controller)
@GetMapping("/api/data")
public ResponseEntity<?> getData(@RequestHeader("User-Id") String userId) {
    if (!rateLimitService.isAllowed(userId)) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");
    }

    // 실제 로직
    return ResponseEntity.ok(data);
}
```

### 6. 블랙리스트 토큰 관리

```java
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisService redisService;

    // 로그아웃 시 토큰을 블랙리스트에 추가
    public void addToBlacklist(String token) {
        String key = "blacklist:" + token;
        // 토큰 만료 시간만큼만 저장
        Long ttl = jwtTokenProvider.getExpirationTime(token);
        redisService.save(key, "blacklisted", Duration.ofSeconds(ttl));
    }

    // 블랙리스트 체크
    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        return redisService.exists(key);
    }
}
```

## 💡 해커톤 꿀팁

### 1. 로컬 개발 시 Redis 없이 테스트
```yaml
# application-test.yml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  # Embedded Redis 사용 (테스트용)
  redis:
    embedded:
      enabled: true
```

### 2. 빠른 디버깅
```bash
# Redis CLI 접속
docker exec -it redis redis-cli

# 모든 키 조회
KEYS *

# 특정 키 조회
GET user:1

# 특정 키 삭제
DEL user:1

# 모든 데이터 삭제 (주의!)
FLUSHALL
```

### 3. 성능 모니터링
```bash
# Redis 상태 확인
docker exec -it redis redis-cli INFO

# 실시간 모니터링
docker exec -it redis redis-cli MONITOR
```

## ⚠️ 주의사항

1. **메모리 관리**: TTL 설정 필수 (메모리 부족 방지)
2. **직렬화 이슈**: 저장할 객체는 Serializable 구현 또는 JSON 직렬화 가능해야 함
3. **패턴 검색 주의**: `KEYS *` 는 프로덕션에서 사용 금지 (성능 이슈)
4. **트랜잭션**: Redis는 단순 트랜잭션만 지원, 복잡한 로직은 Lua 스크립트 사용