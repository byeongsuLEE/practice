# AOP 시간 측정 모듈

메서드 실행 시간을 자동으로 측정하고 로깅하는 AOP 모듈

## 📦 필요한 의존성

```gradle
// build.gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-aop'
}
```

## 📁 파일 구조

```
src/main/java/com/yourproject/
├── common/
│   ├── annotation/
│   │   └── TimeTrace.java          # 어노테이션
│   └── aop/
│       └── TimeTraceAspect.java    # AOP 구현
```

## 🚀 사용 방법

### 1. 파일 복사
- `TimeTrace.java` → `src/main/java/com/yourproject/common/annotation/`
- `TimeTraceAspect.java` → `src/main/java/com/yourproject/common/aop/`

### 2. 패키지명 수정
```java
// 두 파일 모두 패키지명을 프로젝트에 맞게 수정
package com.yourproject.common.annotation;  // -> 본인 프로젝트 패키지
```

### 3. 어노테이션 사용

#### 메서드에 적용
```java
@Service
public class UserService {

    @TimeTrace  // 이 메서드만 시간 측정
    public User findUser(Long id) {
        return userRepository.findById(id);
    }
}
```

#### 클래스 전체에 적용
```java
@TimeTrace  // 이 클래스의 모든 public 메서드 시간 측정
@Service
public class UserService {

    public User findUser(Long id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
```

## 📊 로그 출력 예시

```
2026-01-09 12:34:56 INFO  [TimeTrace] com.example.service.UserService.findUser 시작
2026-01-09 12:34:56 INFO  [TimeTrace] com.example.service.UserService.findUser 종료 - 실행시간: 145ms
```

## 💡 팁

- **성능 테스트**: 해커톤에서 어떤 API가 느린지 빠르게 파악 가능
- **디버깅**: 메서드 호출 순서와 시간을 쉽게 추적
- **프로덕션**: 운영 환경에서는 로그 레벨 조정 권장

## ⚙️ 커스터마이징

### 특정 패키지만 적용하고 싶다면
```java
@Around("execution(* com.yourproject.service.*.*(..)) && (@annotation(timeTrace) || @within(timeTrace))")
```

### 실행 시간이 긴 메서드만 경고하고 싶다면
```java
if (executionTime > 1000) {
    log.warn("[TimeTrace] {}.{} 종료 - 실행시간: {}ms (느림!)", className, methodName, executionTime);
} else {
    log.info("[TimeTrace] {}.{} 종료 - 실행시간: {}ms", className, methodName, executionTime);
}
```