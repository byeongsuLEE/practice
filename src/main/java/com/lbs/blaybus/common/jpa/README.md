# JPA Auditing Base Entity 모듈

생성일/수정일/생성자/수정자 자동 관리

## 📁 파일 구조

```
src/main/java/com/yourproject/
├── domain/common/
│   └── BaseEntity.java      # Base Entity
└── config/
    └── JpaConfig.java         # JPA Auditing 설정
```

## 🚀 사용 방법

### 1. BaseEntity 상속

```java
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // 생성일, 수정일, 생성자, 수정자는 BaseEntity에서 자동 관리됨
}
```

### 2. 자동으로 관리되는 필드

```java
// User 생성
User user = new User("홍길동", "hong@example.com");
userRepository.save(user);

// 자동으로 설정됨:
// - createdDate: 2026-01-09T12:00:00
// - createdBy: "user@example.com" (현재 로그인한 사용자)
// - lastModifiedDate: 2026-01-09T12:00:00
// - lastModifiedBy: "user@example.com"

// User 수정
user.updateName("김철수");
userRepository.save(user);

// 자동으로 업데이트됨:
// - lastModifiedDate: 2026-01-09T12:05:00 (현재 시각으로 업데이트)
// - lastModifiedBy: "admin@example.com" (수정한 사용자)
```

### 3. 조회 시 사용

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .createdBy(user.getCreatedBy())
                .lastModifiedBy(user.getLastModifiedBy())
                .build();
    }
}
```

## 📝 응답 예시

```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com",
  "createdDate": "2026-01-09T12:00:00",
  "lastModifiedDate": "2026-01-09T12:05:00",
  "createdBy": "user@example.com",
  "lastModifiedBy": "admin@example.com"
}
```

## 💡 해커톤 꿀팁

### 1. 생성일만 필요한 경우

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    // createdBy, lastModifiedBy 제거
}
```

### 2. 로그인 없이 테스트

```java
// JpaConfig.java의 auditorProvider 수정
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("TEST_USER");  // 항상 "TEST_USER" 반환
}
```

### 3. 데이터 변경 이력 추적

```java
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // 누가 언제 생성/수정했는지 자동 추적됨
}

// Service에서 활용
public void updatePost(Long postId, UpdateRequest request) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

    // 권한 체크: 본인이 작성한 글만 수정 가능
    String currentUser = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    if (!post.getCreatedBy().equals(currentUser)) {
        throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    post.update(request);
    postRepository.save(post);
    // lastModifiedBy, lastModifiedDate 자동 업데이트
}
```

### 4. 생성일 기준 최근 게시물 조회

```java
public interface PostRepository extends JpaRepository<Post, Long> {

    // BaseEntity의 createdDate 필드 사용
    List<Post> findTop10ByOrderByCreatedDateDesc();

    // 특정 기간 내 생성된 게시물
    List<Post> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}
```

### 5. 엔티티별로 다르게 설정

```java
// 시스템 자동 생성 엔티티 (생성자/수정자 불필요)
@Entity
public class SystemLog extends BaseTimeEntity {  // BaseEntity 대신 BaseTimeEntity
    // ...
}

// 사용자가 생성하는 엔티티 (생성자/수정자 필요)
@Entity
public class Post extends BaseEntity {
    // ...
}
```

## 🔧 커스터마이징

### 1. Long 타입 사용자 ID 저장

```java
// BaseEntity.java 수정
@CreatedBy
private Long createdById;  // String -> Long

@LastModifiedBy
private Long lastModifiedById;

// AuditorAwareImpl 수정
class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(0L);  // 시스템 ID
        }

        // CustomUserDetails에서 userId 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Optional.of(userDetails.getUserId());
    }
}
```

### 2. 소프트 삭제 추가

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    // 소프트 삭제 필드 추가
    private LocalDateTime deletedDate;
    private String deletedBy;
    private boolean deleted = false;

    public void delete(String deletedBy) {
        this.deleted = true;
        this.deletedDate = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
```

### 3. 타임존 설정

```yaml
# application.yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          time_zone: Asia/Seoul
```

## ⚠️ 주의사항

1. **@EnableJpaAuditing 필수**: JpaConfig에 반드시 추가
2. **트랜잭션 필요**: save() 호출 시 트랜잭션 내에서 실행
3. **Setter 금지**: BaseEntity 필드는 자동 관리되므로 Setter 만들지 말 것
4. **@MappedSuperclass**: BaseEntity는 테이블로 생성되지 않음

## 📊 실전 활용

```java
// 최근 7일간 생성된 사용자 통계
public long getNewUsersCount() {
    LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
    return userRepository.countByCreatedDateAfter(weekAgo);
}

// 특정 사용자가 작성한 게시물 조회
public List<Post> getMyPosts(String email) {
    return postRepository.findByCreatedBy(email);
}

// 최근 수정된 게시물 조회
public List<Post> getRecentlyUpdatedPosts() {
    return postRepository.findTop10ByOrderByLastModifiedDateDesc();
}
```