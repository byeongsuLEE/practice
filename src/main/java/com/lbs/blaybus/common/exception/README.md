# 전역 예외 처리 모듈

통일된 API 응답 형태와 전역 예외 처리

## 📁 파일 구조

```
src/main/java/com/yourproject/common/
├── response/
│   ├── ApiResponse.java        # 통일된 API 응답 포맷
│   └── ErrorCode.java           # 에러 코드 정의
└── exception/
    ├── GlobalExceptionHandler.java   # 전역 예외 처리
    └── BusinessException.java        # 커스텀 비즈니스 예외
```

## 🚀 사용 방법

### 1. 파일 복사
- 위 4개 파일을 프로젝트에 복사
- 패키지명을 프로젝트에 맞게 수정

### 2. 컨트롤러에서 성공 응답

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 데이터만 반환
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(
            ApiResponse.success(HttpStatus.OK, new UserResponse(user))
        );
    }

    // 데이터 + 메시지
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        User user = userService.create(request);
        return ResponseEntity.ok(
            ApiResponse.success(HttpStatus.CREATED, "사용자가 생성되었습니다", new UserResponse(user))
        );
    }

    // 메시지만 (데이터 없음)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(
            ApiResponse.success(HttpStatus.OK, "사용자가 삭제되었습니다")
        );
    }
}
```

### 3. 비즈니스 예외 던지기

```java
@Service
public class UserService {

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public void createUser(UserRequest request) {
        // 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE);
        }

        // 사용자 생성 로직
        userRepository.save(new User(request));
    }
}
```

### 4. 새로운 ErrorCode 추가

```java
public enum ErrorCode {
    // 기존 코드들...

    // 새로운 에러 코드 추가
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
    PAYMENT_FAILED(HttpStatus.PAYMENT_REQUIRED, "결제에 실패했습니다");

    private final HttpStatus status;
    private final String message;
}
```

### 5. 새로운 커스텀 예외 추가 (선택사항)

```java
// UserNotFoundException.java
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}

// 사용
throw new UserNotFoundException();
```

## 📝 API 응답 형태

### 성공 응답
```json
{
  "status": "OK",
  "message": "사용자가 생성되었습니다",
  "data": {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com"
  }
}
```

### 에러 응답
```json
{
  "status": "NOT_FOUND",
  "message": "해당 유저를 찾을 수 없습니다"
}
```

### Validation 에러 응답
```json
{
  "status": "BAD_REQUEST",
  "message": "입력값 검증 실패",
  "data": {
    "email": "이메일 형식이 올바르지 않습니다",
    "password": "비밀번호는 8자 이상이어야 합니다"
  }
}
```

## 💡 해커톤 꿀팁

### 1. DTO Validation 자동 검증

```java
public class UserRequest {
    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @Size(min = 8, message = "비밀번리는 8자 이상이어야 합니다")
    private String password;
}

@PostMapping
public ResponseEntity<ApiResponse<UserResponse>> createUser(
    @Valid @RequestBody UserRequest request) {  // @Valid 추가
    // Validation 실패 시 자동으로 GlobalExceptionHandler가 처리
    User user = userService.create(request);
    return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED, new UserResponse(user)));
}
```

### 2. 빠른 에러 코드 추가
필요할 때마다 ErrorCode enum에 추가하면 됨 (1분 컷)

```java
ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"),
INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다"),
```

### 3. 로깅 자동화
GlobalExceptionHandler에서 모든 예외를 자동 로깅하므로 별도로 로깅 코드 작성 불필요

### 4. 프론트엔드 친화적
응답 형태가 통일되어 있어 프론트엔드에서 에러 처리가 간단함

```javascript
// axios 예시
try {
  const response = await axios.get('/api/users/1');
  console.log(response.data.data);  // 실제 데이터
  console.log(response.data.message);  // 메시지
} catch (error) {
  // 에러 처리
  alert(error.response.data.message);
}
```

## 🔧 커스터마이징

### HTTP 상태 코드 변경

```java
// 404가 아닌 200으로 반환하고 싶다면
@ExceptionHandler(BusinessException.class)
public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException e) {
    return ResponseEntity
            .status(HttpStatus.OK)  // 여기를 OK로 변경
            .body(ApiResponse.error(e.getErrorCode().getStatus(), e.getMessage()));
}
```

### 에러 응답에 타임스탬프 추가

```java
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final HttpStatus status;
    private final String message;
    private final T data;
    private final Long timestamp = System.currentTimeMillis();  // 추가
}
```

## ⚠️ 주의사항

1. **민감한 정보 노출 금지**: 에러 메시지에 DB 정보, 시스템 경로 등을 포함하지 말 것
2. **로그는 서버에만**: 사용자에게는 간단한 메시지만, 로그에는 상세 정보 기록
3. **일관성 유지**: 모든 API가 같은 응답 형태를 사용하도록 강제